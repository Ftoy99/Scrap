package net.fabricmc.scrap.item.custom;

import net.fabricmc.scrap.item.inventory.PouchInventory;
import net.fabricmc.scrap.screens.ModScreenHandlers;
import net.fabricmc.scrap.screens.PouchScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.OptionalInt;


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
        NamedScreenHandlerFactory screenHandlerFactory = createScreenHandlerFactory(stack);
        if (screenHandlerFactory!=null){
            player.openHandledScreen(screenHandlerFactory);
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    private NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack) {
//        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) ->
//                 new PouchScreenHandler(i, playerInventory, new PouchInventory(stack)), stack.getName());
        return new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) ->
                new PouchScreenHandler(ModScreenHandlers.POUCH_SCREEN_HANDLER,syncId,playerInventory,new PouchInventory(stack)),stack.getName());

    }

}
