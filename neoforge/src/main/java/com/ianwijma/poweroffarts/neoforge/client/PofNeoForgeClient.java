package com.ianwijma.poweroffarts.neoforge.client;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.client.GasHudRenderer;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class PofNeoForgeClient {

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gas_hud"),
                (graphics, deltaTracker) -> GasHudRenderer.render(graphics));
    }
}
