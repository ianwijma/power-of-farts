package com.ianwijma.poweroffarts.item;

import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.ianwijma.poweroffarts.platform.Services;

import java.util.function.Supplier;

public final class PofDataComponents {

    public static final Supplier<DataComponentType<Double>> GAS_AMOUNT =
            Services.REGISTRY.register(BuiltInRegistries.DATA_COMPONENT_TYPE, "gas_amount", id -> DataComponentType.<Double>builder()
                    .persistent(Codec.DOUBLE)
                    .networkSynchronized(StreamCodec.composite(
                            ByteBufCodecs.DOUBLE, Double::doubleValue, Double::valueOf))
                    .build());

    public static void touch() {
    }
}
