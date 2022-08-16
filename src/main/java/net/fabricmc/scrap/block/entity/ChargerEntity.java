package net.fabricmc.scrap.block.entity;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.scrap.item.inventory.ImplementedInventory;
import net.fabricmc.scrap.recipe.ChargingRecipes;
import net.fabricmc.scrap.screens.ChargerScreenHandler;
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
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleBatteryItem;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;

public class ChargerEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(16000, 16000, 0) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };

    protected final PropertyDelegate propertyDelegate;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private int progress;
    private int cost;
    private int maxProgress;

    public ChargerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHARGER_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return (int) ChargerEntity.this.energyStorage.amount;
                    case 1:
                        return ChargerEntity.this.progress;
                    case 2:
                        return (int) ChargerEntity.this.energyStorage.capacity;
                    case 3:
                        return ChargerEntity.this.cost;
                    case 4:
                        return ChargerEntity.this.maxProgress;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        ChargerEntity.this.energyStorage.amount = value;
                        break;
                    case 1:
                        ChargerEntity.this.progress = value;
                        break;
                    case 3:
                        ChargerEntity.this.cost = value;
                        break;
                    case 4:
                        ChargerEntity.this.maxProgress = value;
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


    public static void tick(World world, BlockPos pos, BlockState state, ChargerEntity entity) {
        if (!world.isClient) {
            if (entity.inventory.get(0).getItem() instanceof SimpleBatteryItem) {
                EnergyStorage maybeEnergyStorage = EnergyStorage.ITEM.find(entity.inventory.get(0), ContainerItemContext.withInitial(entity.inventory.get(0)));
                if (maybeEnergyStorage != null) {
                    if (maybeEnergyStorage.getAmount() == maybeEnergyStorage.getCapacity()) {
                        System.out.println("full");
                        return;
                    }
                    try (Transaction transaction = Transaction.openOuter()) {
                        long inserted;
                        if (entity.energyStorage.amount > 100L) {
                            inserted = maybeEnergyStorage.insert(100L, transaction);
                        } else {
                            if (entity.energyStorage.amount > 0L) {
                                inserted = maybeEnergyStorage.insert(entity.energyStorage.amount, transaction);
                            }else {
                                inserted=0L;
                            }

                        }
                        entity.energyStorage.amount -= inserted;
                        transaction.commit();
                        System.out.println("Inserted amount:"+inserted);
                    }
                    SimpleBatteryItem.setStoredEnergyUnchecked(entity.inventory.get(0),maybeEnergyStorage.getAmount());
                }
            }
            if (hasRecipe(entity)) {
                ChargingRecipes recipe = getRecipe(entity);
                entity.maxProgress = recipe.getTime();
                entity.cost = recipe.getConsumption();
                if (entity.energyStorage.amount > recipe.getConsumption()) {
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
    }

    private static ChargingRecipes getRecipe(ChargerEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        return world.getRecipeManager()
                .getFirstMatch(ChargingRecipes.Type.INSTANCE, inventory, world).get();
    }

    private static void craftItem(ChargerEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<ChargingRecipes> match = world.getRecipeManager()
                .getFirstMatch(ChargingRecipes.Type.INSTANCE, inventory, world);

        if (match.isPresent()) {
            entity.removeStack(0, 1);
            entity.setStack(1, new ItemStack(match.get().getOutput().getItem(),
                    entity.getStack(1).getCount() + 1));
            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(ChargerEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<ChargingRecipes> match = world.getRecipeManager()
                .getFirstMatch(ChargingRecipes.Type.INSTANCE, inventory, world);


        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, ItemStack output) {
        return inventory.getStack(1).getItem() == output.getItem() || inventory.getStack(1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(1).getMaxCount() > inventory.getStack(1).getCount();
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 0;
        this.cost = 0;
    }

    private boolean isFull() {
        return this.energyStorage.amount >= this.energyStorage.capacity;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("charger.progress");
        energyStorage.amount = nbt.getInt("charger.amount");
        cost = nbt.getInt("charger.cost");
        maxProgress = nbt.getInt("charger.maxprogress");

    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
        nbt.putInt("charger.burnTime", progress);
        nbt.putInt("charger.amount", (int) energyStorage.amount);
        nbt.putInt("charger.cost", cost);
        nbt.putInt("charger.maxprogress", maxProgress);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Charger");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ChargerScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

}
