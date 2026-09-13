package com.ianwijma.poweroffarts.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.config.PofConfig;

public final class GasHudRenderer {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/sprites/gas_hud.png");
    private static final int ICONS = 10;
    private static final int ICON_SIZE = 9;
    private static final int FILLED_U = 0;
    private static final int OUTLINE_U = ICON_SIZE;

    public static void render(GuiGraphicsExtractor graphics) {
        PofConfig config = PofConfig.get();
        double stored = ClientGasData.getStoredGas();
        double pending = ClientGasData.getPendingGas();
        if (stored <= 0 && pending <= 0) {
            return;
        }

        double perIcon = config.playerGasCapacity / ICONS;
        int storedIcons = Math.min(ICONS, (int) Math.ceil(stored / (config.playerGasCapacity / (double) ICONS)));
        int pendingIcons = Math.min(ICONS, (int) Math.ceil(pending / (config.playerGasCapacity / (double) ICONS)));

        int y = graphics.guiHeight() - 49;
        for (int i = 0; i < ICONS; i++) {
            int x = graphics.guiWidth() / 2 + 91 - ICON_SIZE - i * 8;
            if (i < pendingIcons) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, OUTLINE_U, 0, ICON_SIZE, ICON_SIZE, 18, 9);
            }
            if (i < storedIcons) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, FILLED_U, 0, ICON_SIZE, ICON_SIZE, 18, 9);
            }
        }
    }

    private GasHudRenderer() {
    }
}
