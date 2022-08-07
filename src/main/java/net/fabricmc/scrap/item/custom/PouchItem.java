package net.fabricmc.scrap.item.custom;

import net.fabricmc.scrap.item.inventory.PouchInventory;
import net.fabricmc.scrap.screens.PouchScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class PouchItem extends Item {
    public PouchItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canBeNested() {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(player.getStackInHand(hand));
        }
        player.setCurrentHand(hand);
        ItemStack stack = player.getStackInHand(hand);
        player.openHandledScreen(createScreenHandlerFactory(stack));
        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    private NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack) {
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) ->
                PouchScreenHandler.createGeneric9x3(i, playerInventory, new PouchInventory(stack)), stack.getName());
    }
}
