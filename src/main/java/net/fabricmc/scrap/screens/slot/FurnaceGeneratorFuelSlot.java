/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.fabricmc.scrap.screens.slot;

import net.fabricmc.scrap.screens.FurnaceGeneratorScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.FurnaceFuelSlot;
import net.minecraft.screen.slot.Slot;

public class FurnaceGeneratorFuelSlot
extends Slot {
    private final PlayerEntity player;
    private int amount;
    private final FurnaceGeneratorScreenHandler handler;

    public FurnaceGeneratorFuelSlot(FurnaceGeneratorScreenHandler handler,PlayerEntity player, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
        this.handler = handler;
    }


    @Override
    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }
        return super.takeStack(amount);
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);
    }

    @Override
    protected void onCrafted(ItemStack stack) {
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return this.handler.isFuel(stack);
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return FurnaceFuelSlot.isBucket(stack) ? 1 : super.getMaxItemCount(stack);
    }
}

