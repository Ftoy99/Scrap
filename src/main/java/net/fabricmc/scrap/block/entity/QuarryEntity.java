package net.fabricmc.scrap.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.scrap.item.custom.SpatialMarkerItem;
import net.fabricmc.scrap.item.inventory.ImplementedInventory;
import net.fabricmc.scrap.screens.QuarryScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.PositionImpl;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.List;

public class QuarryEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(128000, 128000, 0) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    protected final PropertyDelegate propertyDelegate;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private final int cost = 512;

    public QuarryEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.QUARRY_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return (int) QuarryEntity.this.energyStorage.amount;
                    case 1:
                        return (int) QuarryEntity.this.energyStorage.capacity;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        QuarryEntity.this.energyStorage.amount = value;
                        break;
                    default:
                        break;
                }
            }

            public int size() {
                return 2;
            }
        };
    }


    public static void tick(World world, BlockPos pos, BlockState state, QuarryEntity entity) {
        if (!world.isClient()) {
            if (entity.energyStorage.amount < entity.cost){
                return;
            }
            System.out.println("has Energy");
            Item item = entity.inventory.get(0).getItem();
            if (item instanceof SpatialMarkerItem) {
                System.out.println("has marker");
                ItemStack stack = entity.inventory.get(0);
                NbtCompound pos1 = stack.getSubNbt("pos1");
                NbtCompound pos2 = stack.getSubNbt("pos2");
                if (pos1 != null && pos2 != null) {
                    //Has positions mine.
                    System.out.println("has positions");
                    int x, y, z;
                    int x1 = Math.min(pos1.getInt("x"), pos2.getInt("x"));
                    int x2 = Math.max(pos1.getInt("x"), pos2.getInt("x"));
                    int y1 = Math.min(pos1.getInt("y"), pos2.getInt("y"));
                    int y2 = Math.max(pos1.getInt("y"), pos2.getInt("y"));
                    int z1 = Math.max(pos1.getInt("z"), pos2.getInt("z"));
                    int z2 = Math.min(pos1.getInt("z"), pos2.getInt("z"));
                    for (x = x1; x <= x2; x++) {
                        for (y = y1; y <= y2; y++) {
                            for (z = z1; z >= z2; z--) {
                                BlockPos blockPos = new BlockPos(x,y,z);
                                BlockState blockState = world.getBlockState(blockPos);
                                if (!blockState.isOf(Blocks.AIR) && !blockState.isOf(Blocks.BEDROCK) && blockState.isSolidBlock(world,blockPos)){
                                    world.breakBlock(blockPos,false);
                                    LootContext.Builder builder = (new LootContext.Builder((ServerWorld) world)).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockPos)).parameter(LootContextParameters.TOOL, new ItemStack(Items.NETHERITE_PICKAXE));
                                    List<ItemStack> items = blockState.getDroppedStacks(builder);
                                    entity.energyStorage.amount-= entity.cost;
                                    Storage maybeChest = ItemStorage.SIDED.find(world, pos.offset(Direction.UP, 1),null,null,null);
                                    for (ItemStack itemMined : items){
                                        if (maybeChest!=null) {
                                            ItemVariant itemVariant = ItemVariant.of(itemMined.getItem());
                                            try (Transaction transaction = Transaction.openOuter()) {
                                                if (maybeChest.insert(itemVariant, 1, transaction)==1){
                                                    transaction.commit();
                                                    return;
                                                }
                                            }
                                            spawnItem(world, new PositionImpl(pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5), itemMined);
                                            return;
                                        }else {
                                            spawnItem(world, new PositionImpl(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), itemMined);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void spawnItem(World world ,PositionImpl pos ,ItemStack stack) {
        double d = pos.getX();
        double e = pos.getY();
        double f = pos.getZ();

        ItemEntity itemEntity = new ItemEntity(world, d, e, f, stack);
        itemEntity.setVelocity(0,0.75, 0);
        world.spawnEntity(itemEntity);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        energyStorage.amount = nbt.getInt("quarry.amount");

    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
        nbt.putInt("quarry.amount", (int) energyStorage.amount);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Quarry");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new QuarryScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

}
