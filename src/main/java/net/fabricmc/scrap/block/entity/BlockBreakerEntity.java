package net.fabricmc.scrap.block.entity;

import net.fabricmc.scrap.item.inventory.ImplementedInventory;
import net.fabricmc.scrap.screens.BlockBreakerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class BlockBreakerEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(16000, 16000, 0) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    protected final PropertyDelegate propertyDelegate;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private final int breakCost = 100;

    public BlockBreakerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLOCK_BREAKER_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return (int) BlockBreakerEntity.this.energyStorage.amount;
                    case 1:
                        return (int) BlockBreakerEntity.this.energyStorage.capacity;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        BlockBreakerEntity.this.energyStorage.amount = value;
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


    public static void tick(World world, BlockPos pos, BlockState state, BlockBreakerEntity entity) {
        if (!world.isClient()) {
            Direction direction = state.get(HorizontalFacingBlock.FACING).getOpposite();
            BlockPos blockToBreakPos = pos.offset(direction);
            Item item = world.getBlockState(blockToBreakPos).getBlock().asItem();
            ItemStack itemStack = new ItemStack(item);
            if (canFit(entity, itemStack)) {
                if (entity.energyStorage.getAmount()>entity.breakCost){
                    entity.energyStorage.amount-=entity.breakCost;
                    world.removeBlock(blockToBreakPos, false);
                    for (int i = 0; i < entity.inventory.size(); i++) {
                        if (entity.getStack(i).getItem() == itemStack.getItem() && entity.getStack(i).getCount() < entity.getStack(i).getMaxCount()) {
                            entity.setStack(i, new ItemStack(item, entity.getStack(i).getCount() + 1));
                            return;
                        }
                        if (entity.getStack(i) == ItemStack.EMPTY) {
                            entity.setStack(i, new ItemStack(item, 1));
                            return;
                        }
                    }
                }

            } else {
                return;
            }
        }
    }

    private static boolean canFit(BlockBreakerEntity entity, ItemStack itemStack) {
        for (int i = 0; i < entity.inventory.size(); i++) {
            if (entity.getStack(i).getItem() == itemStack.getItem() && entity.getStack(i).getCount() < entity.getStack(i).getMaxCount()) {
                return true;
            }
            if (entity.getStack(i) == ItemStack.EMPTY) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        energyStorage.amount = nbt.getInt("blockbreaker.amount");

    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
        nbt.putInt("blockbreaker.amount", (int) energyStorage.amount);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Block Breaker");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new BlockBreakerScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

}
