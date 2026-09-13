package com.ianwijma.poweroffarts.neoforge;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.client.ClientGasData;
import com.ianwijma.poweroffarts.network.PlayerGasPayload;

public class PofPayloadHandlers {

    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(PlayerGasPayload.TYPE, PlayerGasPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    if (payload.storedGas() > 0 || payload.pendingGas() > 0) {
                        Constants.LOG.debug("Client received gas payload: stored={}, pending={}", payload.storedGas(), payload.pendingGas());
                    }
                    ClientGasData.update(payload.storedGas(), payload.pendingGas());
                }));
    }
}