package net.fabricmc.scrap.block.entity;

import net.fabricmc.scrap.item.inventory.ImplementedInventory;
import net.fabricmc.scrap.screens.BlockPlacerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
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

public class BlockPlacerEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(16000, 16000, 0) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    protected final PropertyDelegate propertyDelegate;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private final int placeCost = 100;

    public BlockPlacerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLOCK_PLACER_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return (int) BlockPlacerEntity.this.energyStorage.amount;
                    case 1:
                        return (int) BlockPlacerEntity.this.energyStorage.capacity;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        BlockPlacerEntity.this.energyStorage.amount = value;
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


    public static void tick(World world, BlockPos pos, BlockState state, BlockPlacerEntity entity) {
        if (!world.isClient()) {
            Direction direction = state.get(HorizontalFacingBlock.FACING).getOpposite();
            BlockPos blockToPlacePos = pos.offset(direction);
            if (entity.energyStorage.amount >= entity.placeCost) {
                if (world.getBlockState(blockToPlacePos).getBlock() == Blocks.AIR) {
                    //place block
                    for (int i = 0; i < entity.inventory.size(); i++) {
                        if (entity.getStack(i)!=ItemStack.EMPTY && entity.getStack(i).getItem() instanceof BlockItem) {
                            if(world.setBlockState(blockToPlacePos,((BlockItem)entity.getStack(i).getItem()).getBlock().getDefaultState())){
                                entity.getStack(i).setCount(entity.getStack(i).getCount()-1);
                                entity.energyStorage.amount -= entity.placeCost;
                            }
                            return;
                        }
                    }
                }
            }

        }
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        energyStorage.amount = nbt.getInt("blockplacer.amount");

    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
        nbt.putInt("blockplacer.amount", (int) energyStorage.amount);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Block Placer");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new BlockPlacerScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

}
