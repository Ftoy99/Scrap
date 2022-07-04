package net.fabricmc.scrap.screens;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.scrap.Main;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandler {
    public static ScreenHandlerType<OreWasherScreenHandler> ORE_WASHER_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(Main.MOD_ID, "ore_washer"),
                    OreWasherScreenHandler::new);
}
