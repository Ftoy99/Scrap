package net.fabricmc.scrap.screens.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class PouchSlot extends Slot {
    public PouchSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return canMoveStack(getStack());
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return canMoveStack(stack);
    }

    // Prevents items that override canBeNested() from being inserted into backpack
    public boolean canMoveStack(ItemStack stack) {
        return stack.getItem().canBeNested();
    }
}
