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

public class BlockPlacerScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final World world;
    private final PropertyDelegate propertyDelegate;

    public BlockPlacerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(9),new ArrayPropertyDelegate(2));
    }

    public BlockPlacerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate) {
        super(ModScreenHandler.BLOCK_PLACER_SCREEN_HANDLER, syncId);
        checkSize(inventory, 9);
        this.inventory = inventory;
        this.world = playerInventory.player.world;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = delegate;

        // Our Slots
        this.addSlot(new Slot(inventory, 0, 62, 17));
        this.addSlot(new Slot(inventory, 1, 80, 17));
        this.addSlot(new Slot(inventory, 2, 98, 17));
        this.addSlot(new Slot(inventory, 3, 62, 35));
        this.addSlot(new Slot(inventory, 4, 80, 35));
        this.addSlot(new Slot(inventory, 5, 98, 35));
        this.addSlot(new Slot(inventory, 6, 62, 53));
        this.addSlot(new Slot(inventory, 7, 80, 53));
        this.addSlot(new Slot(inventory, 8, 98, 53));
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
        int i = this.propertyDelegate.get(1);
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

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
