package net.fabricmc.scrap.block.entity;

import net.fabricmc.scrap.item.inventory.ImplementedInventory;
import net.fabricmc.scrap.recipe.AlloyingRecipes;
import net.fabricmc.scrap.screens.AlloySmelterScreenHandler;
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

public class AlloySmelterEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(16000, 16000, 0) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    protected final PropertyDelegate propertyDelegate;
    public final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private int progress;
    private int cost;
    private int maxProgress;

    public AlloySmelterEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALLOY_SMELTER_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return (int) AlloySmelterEntity.this.energyStorage.amount;
                    case 1:
                        return (int) AlloySmelterEntity.this.energyStorage.capacity;
                    case 2:
                        return AlloySmelterEntity.this.progress;
                    case 3:
                        return AlloySmelterEntity.this.cost;
                    case 4:
                        return AlloySmelterEntity.this.maxProgress;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        AlloySmelterEntity.this.energyStorage.amount = value;
                        break;
                    case 2:
                        AlloySmelterEntity.this.progress = value;
                        break;
                    case 3:
                        AlloySmelterEntity.this.cost = value;
                        break;
                    case 4:
                        AlloySmelterEntity.this.maxProgress = value;
                        break;
                    default:
                        break;
                }
            }

            public int size() {
                return 5;
            }
        };
    }




    public static void tick(World world, BlockPos pos, BlockState state, AlloySmelterEntity entity) {
            if (hasRecipe(entity)) {
                AlloyingRecipes recipe = getRecipe(entity);
                entity.maxProgress = recipe.getTime();
                entity.cost = recipe.getConsumption();
                if (entity.energyStorage.amount>recipe.getConsumption()) {
                    entity.progress++;
                    entity.energyStorage.amount -= recipe.getConsumption();
                    if (entity.progress >= recipe.getTime()) {
                        craftItem(entity);
                    }
                }
            } else {
                entity.resetProgress();
            }

    }
    private static void craftItem(AlloySmelterEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<AlloyingRecipes> match = world.getRecipeManager()
                .getFirstMatch(AlloyingRecipes.Type.INSTANCE, inventory, world);
        //Remove items when crafted from input slots
        if (match.isPresent()) {
            if (entity.getStack(0).getItem()==match.get().getInput1().getItem()){
                entity.removeStack(0, match.get().getInput1().getCount());
                entity.removeStack(1, match.get().getInput2().getCount());
            }else {
                entity.removeStack(1, match.get().getInput1().getCount());
                entity.removeStack(0, match.get().getInput2().getCount());
            }
            //Add item to output slot
            entity.setStack(2, new ItemStack(match.get().getOutput().getItem(),
                    entity.getStack(2).getCount() + match.get().getOutput().getCount()));
            entity.resetProgress();
        }
    }
    private void resetProgress() {
        this.progress = 0;
        this.maxProgress =0;
        this.cost = 0;
    }

    private static boolean hasRecipe(AlloySmelterEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<AlloyingRecipes> match = world.getRecipeManager()
                .getFirstMatch(AlloyingRecipes.Type.INSTANCE, inventory, world);
        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput());
    }

    private static AlloyingRecipes getRecipe(AlloySmelterEntity entity){
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        return world.getRecipeManager()
                .getFirstMatch(AlloyingRecipes.Type.INSTANCE, inventory, world).get();
    }

    private boolean isFull() {
        return this.energyStorage.amount >= this.energyStorage.capacity;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, ItemStack output) {
        return inventory.getStack(2).getItem() == output.getItem() || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("alloysmelter.progress");
        energyStorage.amount = nbt.getInt("alloysmelter.amount");
        cost = nbt.getInt("alloysmelter.cost");
        maxProgress = nbt.getInt("alloysmelter.maxprogress");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
        nbt.putInt("alloysmelter.progress", progress);
        nbt.putInt("alloysmelter.amount", (int) energyStorage.amount);
        nbt.putInt("alloysmelter.cost",cost);
        nbt.putInt("alloysmelter.maxprogress",maxProgress);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Alloy Smelter");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new AlloySmelterScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

}
