package com.ianwijma.poweroffarts.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.client.ClientGasData;
import com.ianwijma.poweroffarts.client.GasHudRenderer;
import com.ianwijma.poweroffarts.network.PlayerGasPayload;

public class PowerOfFartsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.FOOD_BAR,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gas_hud"),
                (graphics, deltaTracker) -> GasHudRenderer.render(graphics));

        ClientPlayNetworking.registerGlobalReceiver(PlayerGasPayload.TYPE, (payload, context) ->
                context.client().execute(() ->
                        ClientGasData.update(payload.storedGas(), payload.pendingGas())));
    }
}
