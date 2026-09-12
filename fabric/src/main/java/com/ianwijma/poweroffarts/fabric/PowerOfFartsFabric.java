package com.ianwijma.poweroffarts.fabric;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.PowerOfFarts;
import net.fabricmc.api.ModInitializer;

public class PowerOfFartsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOG.info("Hello Fabric world!");
        PowerOfFarts.init();
    }
}
