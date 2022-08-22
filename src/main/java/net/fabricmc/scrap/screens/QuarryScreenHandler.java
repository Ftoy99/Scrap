package net.fabricmc.scrap.screens;

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

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class QuarryScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public QuarryScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(2));
    }

    public QuarryScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate) {
        super(ModScreenHandlers.QUARRY_SCREEN_HANDLER, syncId);
        checkSize(inventory, 1);
        this.inventory = inventory;
        World world = playerInventory.player.world;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = delegate;

        // Our Slots
        this.addSlot(new Slot(inventory, 0, 80, 41));
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        this.addProperties(propertyDelegate);
    }



    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public String getEnergyTooltip() {
        String tooltip = "";
        int currentEnergy = this.propertyDelegate.get(0);
        NumberFormat formatter = new DecimalFormat("0.0");
        if (currentEnergy > 1000) {
            String current = formatter.format((float) currentEnergy / 1000);
            String capacity = formatter.format((float) this.propertyDelegate.get(1) / 1000);
            tooltip += current + "KSU/" + capacity + "KSU";
        } else {

            tooltip += currentEnergy;
            String capacity = formatter.format((float) this.propertyDelegate.get(1) / 1000);
            tooltip += "SU/" + capacity + "KSU";
        }
        return tooltip;
    }

    public int getEnergyProgress() {
        int maxEnergy = this.propertyDelegate.get(1);
        if (maxEnergy > 0) {
            return this.propertyDelegate.get(0) * 50 / maxEnergy;
        }
        else{
            return 0;
        }

    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
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

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
