package com.ianwijma.poweroffarts.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import com.ianwijma.poweroffarts.Constants;

public record PlayerGasPayload(double storedGas, double pendingGas) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PlayerGasPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "player_gas"));

    public static final StreamCodec<ByteBuf, PlayerGasPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, PlayerGasPayload::storedGas,
            ByteBufCodecs.DOUBLE, PlayerGasPayload::pendingGas,
            PlayerGasPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
