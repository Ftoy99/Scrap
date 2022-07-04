package net.fabricmc.scrap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.scrap.screens.ModScreenHandler;
import net.fabricmc.scrap.screens.OreWasherScreen;
import net.fabricmc.scrap.util.ModRenderHelper;

@Environment(EnvType.CLIENT)
public class ScrapClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModRenderHelper.setRenderLayers();
        ScreenRegistry.register(ModScreenHandler.ORE_WASHER_SCREEN_HANDLER, OreWasherScreen::new);
    }
}