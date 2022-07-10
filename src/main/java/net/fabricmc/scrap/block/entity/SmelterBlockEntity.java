package net.fabricmc.scrap.block.entity;

import com.google.common.collect.Maps;
import net.fabricmc.scrap.Main;
import net.fabricmc.scrap.item.inventory.ImplementedInventory;
import net.fabricmc.scrap.recipe.OreWasherRecipe;
import net.fabricmc.scrap.screens.FurnaceGeneratorScreenHandler;
import net.fabricmc.scrap.screens.SmelterScreenHandler;
import net.minecraft.SharedConstants;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static net.minecraft.state.property.Properties.WATERLOGGED;

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
    private int cost=8;
    private int maxProgress=200;

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




    public static void tick(World world, BlockPos pos, BlockState state, SmelterBlockEntity entity) {

            if (hasRecipe(entity)) {
                if (entity.energyStorage.amount>entity.cost) {
                    Main.LOGGER.info("Has Recipe");
                    entity.progress++;
                    entity.energyStorage.amount -= entity.cost;
                    if (entity.progress > entity.maxProgress) {
                        craftItem(entity);
                    }
                }
            } else {
                entity.resetProgress();
            }

    }
    private static void craftItem(SmelterBlockEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<SmeltingRecipe> match = world.getRecipeManager()
                .getFirstMatch(RecipeType.SMELTING, inventory, world);

        if (match.isPresent()) {
            entity.removeStack(0, 1);
            entity.setStack(1, new ItemStack(match.get().getOutput().getItem(),
                    entity.getStack(1).getCount() + 1));
            entity.resetProgress();
        }
    }
    private void resetProgress() {
        this.progress = 0;
    }
    private static boolean hasRecipe(SmelterBlockEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<SmeltingRecipe> match = world.getRecipeManager()
                .getFirstMatch(RecipeType.SMELTING, inventory, world);
        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput());
    }
    private boolean isFull() {
        return this.energyStorage.amount >= this.energyStorage.capacity;
    }
    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, ItemStack output) {
        return inventory.getStack(1).getItem() == output.getItem() || inventory.getStack(1).isEmpty();
    }
    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(1).getMaxCount() > inventory.getStack(1).getCount();
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
