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

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AlloySmelterScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public AlloySmelterScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(5));
    }

    public AlloySmelterScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate) {
        super(ModScreenHandlers.ALLOY_SMELTER_SCREEN_HANDLER, syncId);
        checkSize(inventory, 3);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = delegate;

        // Our Slots
        this.addSlot(new Slot(inventory, 0, 44, 36));
        this.addSlot(new Slot(inventory, 1, 66, 36));
        this.addSlot(new ScrapOutputSlot(playerInventory.player, inventory, 2, 134, 36));
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        this.addProperties(propertyDelegate);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public int getEnergyProgress() {
        int i = this.propertyDelegate.get(1);
        return this.propertyDelegate.get(0) * 50 / i;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        //Get the transfer slot
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                //Change this 2 to not allow quick transfer to output
            } else if (!this.insertItem(originalStack, 0, 2, false)) {
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

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    public int getProgress() {
        if (this.propertyDelegate.get(4) > 0) {
            return this.propertyDelegate.get(2) * 24 / this.propertyDelegate.get(4);
        } else {
            return 0;
        }

    }
}
