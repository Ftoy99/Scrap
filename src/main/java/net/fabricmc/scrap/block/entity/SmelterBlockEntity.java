package net.fabricmc.scrap.block.entity;

import com.google.common.collect.Maps;
import net.fabricmc.scrap.item.inventory.ImplementedInventory;
import net.fabricmc.scrap.screens.FurnaceGeneratorScreenHandler;
import net.fabricmc.scrap.screens.SmelterScreenHandler;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.LinkedHashMap;
import java.util.Map;

public class SmelterBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(16000, 16000, 0) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    protected final PropertyDelegate propertyDelegate;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    int progress;

    public SmelterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMELTER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return (int) SmelterBlockEntity.this.energyStorage.amount;
                    case 1:
                        return SmelterBlockEntity.this.progress;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        SmelterBlockEntity.this.energyStorage.amount = value;
                        break;
                    case 1:
                        SmelterBlockEntity.this.progress = value;
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




    public static void tick(World world, BlockPos pos, BlockState state, SmelterBlockEntity blockEntity) {
//        boolean bl = blockEntity.isBurning();
//        boolean bl2 = false;
//        if (blockEntity.isBurning()) {
//            --blockEntity.burnTime;
//            //If power not full generate power
//            if (blockEntity.energyStorage.amount < blockEntity.energyStorage.capacity) {
//                if (blockEntity.generates <= blockEntity.energyStorage.capacity - blockEntity.energyStorage.amount) {
//                    blockEntity.energyStorage.amount = blockEntity.energyStorage.amount + blockEntity.generates;
//                }else {
//                    blockEntity.energyStorage.amount = blockEntity.energyStorage.amount +(blockEntity.energyStorage.capacity-blockEntity.energyStorage.amount);
//                }
//
//            }
//        }
//        ItemStack itemStack = blockEntity.inventory.get(0);
//        boolean bl3 = !blockEntity.inventory.get(0).isEmpty();
//        boolean bl4 = !itemStack.isEmpty();
//        if (blockEntity.isBurning() || bl4 && bl3) {
//            int i = blockEntity.getMaxCountPerStack();
//            if (!blockEntity.isBurning() && !blockEntity.isFull()) {
//                blockEntity.fuelTime = blockEntity.burnTime = blockEntity.getFuelTime(itemStack);
//                if (blockEntity.isBurning()) {
//                    bl2 = true;
//                    if (bl4) {
//                        Item item = itemStack.getItem();
//                        itemStack.decrement(1);
//                        if (itemStack.isEmpty()) {
//                            Item item2 = item.getRecipeRemainder();
//                            blockEntity.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
//                        }
//                    }
//                }
//            }
//        }
//        if (bl2) {
//            SmelterBlockEntity.markDirty(world, pos, state);
//        }
    }
    private boolean isFull() {
        return this.energyStorage.amount >= this.energyStorage.capacity;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("furnacegenerator.progress");
        energyStorage.amount = nbt.getInt("furnacegenerator.energy");

    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
        nbt.putInt("furnacegenerator.burnTime", progress);
        nbt.putInt("furnacegenerator.energy", (int) energyStorage.amount);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Smelter");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new SmelterScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

}
