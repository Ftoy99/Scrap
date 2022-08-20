package net.fabricmc.scrap.screens;

import net.fabricmc.scrap.item.custom.PouchItem;
import net.fabricmc.scrap.screens.slot.PouchSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class PouchScreenHandler extends ScreenHandler {
    public final Inventory inventory;

    public PouchScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(27));
    }

    public PouchScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.POUCH_SCREEN_HANDLER, syncId);
        checkSize(inventory, 27);
        inventory.onOpen(playerInventory.player);
        this.inventory = inventory;
//        int i = (this.rows - 4) * 18;
        int i = (3 - 4) * 18;
        int j;
        int k;
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new PouchSlot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new PouchSlot(playerInventory, k + j * 9 + 9, 8 + k * 18, 102 + j * 18 + i));
            }
        }


        for (j = 0; j < 9; ++j) {
            this.addSlot(new PouchSlot(playerInventory, j, 8 + j * 18, 160 + i));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotIndex >= 0)  // slotId < 0 are used for networking internals
        {
            ItemStack stack = getSlot(slotIndex).getStack();

            if (stack.getItem() instanceof PouchItem) {
                return;
            }

        }

        super.onSlotClick(slotIndex, button, actionType, player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (itemStack.getItem() instanceof PouchItem){
                return ItemStack.EMPTY;
            }
            if (index < 3 * 9) {
                if (!this.insertItem(itemStack2, 3 * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, 3 * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }
}