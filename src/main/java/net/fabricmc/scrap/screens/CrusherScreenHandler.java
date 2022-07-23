package net.fabricmc.scrap.screens;

import net.fabricmc.scrap.screens.slot.ScrapOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class CrusherScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final World world;
    private final PropertyDelegate propertyDelegate;

    public CrusherScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(2),new ArrayPropertyDelegate(2));
    }

    public CrusherScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate) {
        super(ModScreenHandler.CRUSHER_SCREEN_HANDLER, syncId);
        checkSize(inventory, 2);
        this.inventory = inventory;
        this.world = playerInventory.player.world;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = delegate;

        // Our Slots
        this.addSlot(new Slot(inventory, 0, 38, 41));
        this.addSlot(new ScrapOutputSlot(playerInventory.player,inventory, 1, 120, 41));
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        this.addProperties(propertyDelegate);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public int getEnergyProgress() {
        int i = 16000;
        return this.propertyDelegate.get(0) * 50 / i;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }
    public String getEnergyTooltip(){
        String tooltip = "";
        int currentEnergy = this.propertyDelegate.get(0);
        if (currentEnergy>1000){
            tooltip+=currentEnergy/1000;
            tooltip+="KSU/16KSU";
        }else {
            tooltip+=currentEnergy;
            tooltip+="SU/16KSU";
        }
        return tooltip;
    }
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    public int getProgress() {
            int i = 200;
            return this.propertyDelegate.get(1) * 24 / i;
    }
}
