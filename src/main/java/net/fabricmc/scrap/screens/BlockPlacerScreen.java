package net.fabricmc.scrap.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.scrap.Main;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BlockPlacerScreen extends HandledScreen<BlockPlacerScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Main.MOD_ID,"textures/gui/block_breaker_gui.png");
    public BlockPlacerScreen(BlockPlacerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        int pow = handler.getEnergyProgress();
        drawTexture(matrices, x + 12, y + 19+50-pow, 176, 50+17-pow, 8, pow);
    }
    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
        if (((ScreenHandler)this.handler).getCursorStack().isEmpty() && isPointOverEnergyBar(12,19,mouseX,mouseY)) {
            this.renderTooltip(matrices, Text.literal(handler.getEnergyTooltip()), mouseX, mouseY);
        }
    }

    private boolean isPointOverEnergyBar(int pointX, int pointY, double mouseX, double mouseY) {
        //Replace width and height with gui size of bar.
        return this.isPointWithinBounds(pointX, pointY, 8, 50, mouseX, mouseY);
    }
}
