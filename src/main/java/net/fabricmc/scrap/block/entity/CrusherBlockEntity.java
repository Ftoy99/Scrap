package net.fabricmc.scrap.block.entity;

import net.fabricmc.scrap.item.inventory.ImplementedInventory;
import net.fabricmc.scrap.recipe.CrushingRecipes;
import net.fabricmc.scrap.screens.CrusherScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;

public class CrusherBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

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

    public CrusherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUSHER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return (int) CrusherBlockEntity.this.energyStorage.amount;
                    case 1:
                        return CrusherBlockEntity.this.progress;
                    case 2:
                        return (int) CrusherBlockEntity.this.energyStorage.capacity;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        CrusherBlockEntity.this.energyStorage.amount = value;
                        break;
                    case 1:
                        CrusherBlockEntity.this.progress = value;
                        break;
                    default:
                        break;
                }
            }

            public int size() {
                return 3;
            }
        };
    }




    public static void tick(World world, BlockPos pos, BlockState state, CrusherBlockEntity entity) {
            if (hasRecipe(entity)) {
                if (entity.energyStorage.amount>entity.cost) {
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
    private static void craftItem(CrusherBlockEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<CrushingRecipes> match = world.getRecipeManager()
                .getFirstMatch(CrushingRecipes.Type.INSTANCE, inventory, world);

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

    private static boolean hasRecipe(CrusherBlockEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<CrushingRecipes> match = world.getRecipeManager()
                .getFirstMatch(CrushingRecipes.Type.INSTANCE, inventory, world);
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
        energyStorage.amount = nbt.getInt("furnacegenerator.amount");

    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
        nbt.putInt("furnacegenerator.burnTime", progress);
        nbt.putInt("furnacegenerator.amount", (int) energyStorage.amount);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Crusher");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CrusherScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

}
