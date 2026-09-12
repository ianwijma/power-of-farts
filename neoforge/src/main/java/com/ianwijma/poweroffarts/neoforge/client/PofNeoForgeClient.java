package com.ianwijma.poweroffarts.neoforge.client;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.client.ClientGasData;
import com.ianwijma.poweroffarts.client.GasHudRenderer;
import com.ianwijma.poweroffarts.network.PlayerGasPayload;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class PofNeoForgeClient {

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gas_hud"),
                (graphics, deltaTracker) -> GasHudRenderer.render(graphics));
    }

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(PlayerGasPayload.TYPE, PlayerGasPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() ->
                        ClientGasData.update(payload.storedGas(), payload.pendingGas())));
    }
}
