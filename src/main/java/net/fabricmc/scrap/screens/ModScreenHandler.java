package net.fabricmc.scrap.screens;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.scrap.Main;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
public class ModScreenHandler {
    public static ScreenHandlerType<OreWasherScreenHandler> ORE_WASHER_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(Main.MOD_ID, "ore_washer"),
                    OreWasherScreenHandler::new);

    public static ScreenHandlerType<FurnaceGeneratorScreenHandler> FURNACE_GENERATOR_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(Main.MOD_ID, "furnace_generator"),
                    FurnaceGeneratorScreenHandler::new);

    public static ScreenHandlerType<SmelterScreenHandler> SMELTER_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(Main.MOD_ID, "smelter"),
                    SmelterScreenHandler::new);

    public static ScreenHandlerType<CrusherScreenHandler> CRUSHER_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(Main.MOD_ID, "crusher"),
                    CrusherScreenHandler::new);

    public static ScreenHandlerType<AlloySmelterScreenHandler> ALLOY_SMELTER_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(Main.MOD_ID, "alloy_smelter"),
                    AlloySmelterScreenHandler::new);

    public static ScreenHandlerType<BlockBreakerScreenHandler> BLOCK_BREAKER_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(Main.MOD_ID, "block_breaker"),
                    BlockBreakerScreenHandler::new);

    public static ScreenHandlerType<BlockPlacerScreenHandler> BLOCK_PLACER_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(Main.MOD_ID, "block_placer"),
                    BlockPlacerScreenHandler::new);}
