package net.fabricmc.scrap.screens;

import net.fabricmc.scrap.Main;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlers {
    public static ScreenHandlerType<PouchScreenHandler> POUCH_SCREEN_HANDLER;
    public static ScreenHandlerType<OreWasherScreenHandler> ORE_WASHER_SCREEN_HANDLER;
    public static ScreenHandlerType<FurnaceGeneratorScreenHandler> FURNACE_GENERATOR_SCREEN_HANDLER;
    public static ScreenHandlerType<SmelterScreenHandler> SMELTER_SCREEN_HANDLER;
    public static ScreenHandlerType<CrusherScreenHandler> CRUSHER_SCREEN_HANDLER;
    public static ScreenHandlerType<ChargerScreenHandler> CHARGER_SCREEN_HANDLER;
    public static ScreenHandlerType<AlloySmelterScreenHandler> ALLOY_SMELTER_SCREEN_HANDLER;
    public static ScreenHandlerType<BlockBreakerScreenHandler> BLOCK_BREAKER_SCREEN_HANDLER;
    public static ScreenHandlerType<BlockPlacerScreenHandler> BLOCK_PLACER_SCREEN_HANDLER;
    public static ScreenHandlerType<QuarryScreenHandler> QUARRY_SCREEN_HANDLER;


    public static void registerModScreens() {
        POUCH_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"pouch_screen_handler"), new ScreenHandlerType<>(PouchScreenHandler::new));
        ORE_WASHER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"ore_washer_screen_handler"), new ScreenHandlerType<>(OreWasherScreenHandler::new));
        FURNACE_GENERATOR_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"furnace_generator_screen_handler"), new ScreenHandlerType<>(FurnaceGeneratorScreenHandler::new));
        SMELTER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"smelter_screen_handler"), new ScreenHandlerType<>(SmelterScreenHandler::new));
        CRUSHER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"crusher_screen_handler"), new ScreenHandlerType<>(CrusherScreenHandler::new));
        CHARGER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"charger_screen_handler"), new ScreenHandlerType<>(ChargerScreenHandler::new));
        ALLOY_SMELTER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"alloy_smelter_screen_handler"), new ScreenHandlerType<>(AlloySmelterScreenHandler::new));
        BLOCK_BREAKER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"block_breaker_screen_handler"), new ScreenHandlerType<>(BlockBreakerScreenHandler::new));
        BLOCK_PLACER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"block_placer_handler"), new ScreenHandlerType<>(BlockPlacerScreenHandler::new));
        QUARRY_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(Main.MOD_ID,"quarry_handler"), new ScreenHandlerType<>(QuarryScreenHandler::new));
    }
}
