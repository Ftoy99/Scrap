package net.fabricmc.scrap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.scrap.screens.*;
import net.fabricmc.scrap.util.ModRenderHelper;

@Environment(EnvType.CLIENT)
public class ScrapClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModRenderHelper.setRenderLayers();
        ScreenRegistry.register(ModScreenHandler.ORE_WASHER_SCREEN_HANDLER, OreWasherScreen::new);
        ScreenRegistry.register(ModScreenHandler.FURNACE_GENERATOR_SCREEN_HANDLER, FurnaceGeneratorScreen::new);
        ScreenRegistry.register(ModScreenHandler.SMELTER_SCREEN_HANDLER, SmelterScreen::new);
        ScreenRegistry.register(ModScreenHandler.CRUSHER_SCREEN_HANDLER, CrusherScreen::new);
        ScreenRegistry.register(ModScreenHandler.ALLOY_SMELTER_SCREEN_HANDLER, AlloySmelterScreen::new);
        ScreenRegistry.register(ModScreenHandler.BLOCK_BREAKER_SCREEN_HANDLER, BlockBreakerScreen::new);
        ScreenRegistry.register(ModScreenHandler.BLOCK_PLACER_SCREEN_HANDLER, BlockPlacerScreen::new);
    }
}