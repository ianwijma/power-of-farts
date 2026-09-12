package com.ianwijma.poweroffarts.gas;

import java.util.function.Supplier;

import com.ianwijma.poweroffarts.platform.Services;

public final class PofGases {

    public static final Supplier<Gas> FARTS = Services.REGISTRY.register(Gases.REGISTRY, "farts", id -> new Gas());

    public static void touch() {
    }
}
