package net.fabricmc.scrap.screens;

import com.google.common.collect.Sets;
import net.fabricmc.scrap.item.custom.PouchItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Set;

public class PouchScreenHandler extends ScreenHandler {
    public static final Set<Item> SHULKER_BOXES;

    static {
        SHULKER_BOXES = Sets.newHashSet(Items.SHULKER_BOX, Items.BLACK_SHULKER_BOX, Items.BLUE_SHULKER_BOX,
                Items.BROWN_SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.GRAY_SHULKER_BOX, Items.GREEN_SHULKER_BOX,
                Items.LIGHT_BLUE_SHULKER_BOX, Items.LIGHT_GRAY_SHULKER_BOX, Items.LIME_SHULKER_BOX,
                Items.MAGENTA_SHULKER_BOX, Items.ORANGE_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.RED_SHULKER_BOX,
                Items.WHITE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX, Items.PURPLE_SHULKER_BOX);
    }

    public final Inventory inventory;
    public final PlayerInventory playerInventory;
    public final int inventoryHeight = 3;
    private final ScreenHandlerType<?> type;

    public PouchScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(27));
        System.out.println(1);
    }

    public PouchScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        this(ModScreenHandlers.POUCH_SCREEN_HANDLER, syncId, playerInventory, inventory);
        System.out.println(2);
    }

    public PouchScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.POUCH_SCREEN_HANDLER, syncId);
        System.out.println(3);
        this.type = type;
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        checkSize(inventory, 27);
        inventory.onOpen(playerInventory.player);
        setupSlots();
    }

    public void setupSlots() {
        int i = (this.inventoryHeight - 4) * 18;
        int n;
        int m;

        for (n = 0; n < this.inventoryHeight; ++n) {
            for (m = 0; m < 9; ++m) {
                this.addSlot(new BackpackSlot(inventory, m + n * 9, 8 + m * 18, 18 + n * 18));
            }
        }

        for (n = 0; n < 3; ++n) {
            for (m = 0; m < 9; ++m) {
                this.addSlot(new BackpackSlot(playerInventory, m + n * 9 + 9, 8 + m * 18, 103 + n * 18 + i));
            }
        }

        for (n = 0; n < 9; ++n) {
            this.addSlot(new BackpackSlot(playerInventory, n, 8 + n * 18, 161 + i));
        }
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return type;
    }

    @Override
    public void onSlotClick(int slotId, int clickData, SlotActionType actionType, PlayerEntity playerEntity) {
        // Prevents single or shift-click while pack is open
        if (slotId >= 0)  // slotId < 0 are used for networking internals
        {
            ItemStack stack = getSlot(slotId).getStack();

            if (stack.getItem() instanceof PouchItem ||
                    SHULKER_BOXES.contains(stack.getItem())) {
                return;
            }

        }

        super.onSlotClick(slotId, clickData, actionType, playerEntity);
    }

    @Override
    public boolean canUse(final PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        ItemStack originalStack = slot.getStack();
        Item testItem = originalStack.getItem();
        if (testItem instanceof PouchItem ||
                SHULKER_BOXES.contains(testItem)) {
            return ItemStack.EMPTY;
        }

        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < 27) {
                if (!this.insertItem(itemStack2, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, 27, false)) {
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

    public static class BackpackSlot extends Slot {
        public BackpackSlot(Inventory inventory, int index, int x, int y) {
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

        public boolean canMoveStack(ItemStack stack) {
            return stack.getItem().canBeNested();
        }
    }
}