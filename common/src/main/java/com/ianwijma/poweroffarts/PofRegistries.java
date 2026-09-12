package com.ianwijma.poweroffarts;

import com.ianwijma.poweroffarts.gas.PofGases;
import com.ianwijma.poweroffarts.item.PofDataComponents;
import com.ianwijma.poweroffarts.item.PofItems;
import com.ianwijma.poweroffarts.machine.PofBlockEntities;
import com.ianwijma.poweroffarts.machine.PofBlocks;

public final class PofRegistries {

    private static boolean initialized;

    public static synchronized void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        PofGases.touch();
        PofDataComponents.touch();
        PofItems.touch();
        PofBlocks.touch();
        PofBlockEntities.touch();
    }

    private PofRegistries() {
    }
}
