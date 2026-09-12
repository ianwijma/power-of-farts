package com.ianwijma.poweroffarts;

import com.ianwijma.poweroffarts.gas.PofGases;

public final class PofRegistries {

    private static boolean initialized;

    public static synchronized void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        PofGases.touch();
    }

    private PofRegistries() {
    }
}
