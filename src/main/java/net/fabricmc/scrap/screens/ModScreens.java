package net.fabricmc.scrap.screens;

import net.fabricmc.scrap.Main;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class ModScreens {
    public static ScreenHandlerType<PouchScreenHandler> POUCH_SCREEN_HANDLER;


    public static void registerModScreens() {
        POUCH_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, Main.MOD_ID, new ScreenHandlerType<>(PouchScreenHandler::new));
    }
}
