package net.fabricmc.scrap.item.custom;

import net.fabricmc.scrap.item.inventory.PouchInventory;
import net.fabricmc.scrap.screens.PouchScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


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
        return new NamedScreenHandlerFactory() {

            @Override
            public Text getDisplayName() {
                return stack.getName();
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new PouchScreenHandler(syncId,inv,new PouchInventory(stack));
            }
        };
    }
}
