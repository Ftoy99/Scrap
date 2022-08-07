package net.fabricmc.scrap.item.inventory;

import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class PouchInventory implements ImplementedInventory{
    private final ItemStack stack;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);

    public PouchInventory(ItemStack stack)
    {
        this.stack = stack;
        NbtCompound tag = stack.getSubNbt("pouch");

        if (tag != null)
        {
            Inventories.readNbt(tag, inventory);
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void markDirty()
    {
        NbtCompound tag = stack.getOrCreateSubNbt("pouch");
        Inventories.writeNbt(tag, inventory);
    }
}
