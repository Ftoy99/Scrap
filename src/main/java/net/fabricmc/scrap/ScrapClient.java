package net.fabricmc.scrap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.scrap.screens.*;
import net.fabricmc.scrap.util.ModRenderHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class ScrapClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModRenderHelper.setRenderLayers();
        HandledScreens.register(ModScreenHandlers.ORE_WASHER_SCREEN_HANDLER, OreWasherScreen::new);
        HandledScreens.register(ModScreenHandlers.FURNACE_GENERATOR_SCREEN_HANDLER, FurnaceGeneratorScreen::new);
        HandledScreens.register(ModScreenHandlers.SMELTER_SCREEN_HANDLER, SmelterScreen::new);
        HandledScreens.register(ModScreenHandlers.CRUSHER_SCREEN_HANDLER, CrusherScreen::new);
        HandledScreens.register(ModScreenHandlers.CHARGER_SCREEN_HANDLER, ChargerScreen::new);
        HandledScreens.register(ModScreenHandlers.ALLOY_SMELTER_SCREEN_HANDLER, AlloySmelterScreen::new);
        HandledScreens.register(ModScreenHandlers.BLOCK_BREAKER_SCREEN_HANDLER, BlockBreakerScreen::new);
        HandledScreens.register(ModScreenHandlers.BLOCK_PLACER_SCREEN_HANDLER, BlockPlacerScreen::new);
        HandledScreens.register(ModScreenHandlers.POUCH_SCREEN_HANDLER, PouchScreen::new);
    }
}