package com.ianwijma.poweroffarts.fabric;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.PofRegistries;
import com.ianwijma.poweroffarts.PowerOfFarts;
import com.ianwijma.poweroffarts.config.PofConfig;
import net.fabricmc.api.ModInitializer;

public class PowerOfFartsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PofRegistries.init();
        PofConfig.load();
        Constants.LOG.info("Hello Fabric world!");
        PowerOfFarts.init();
    }
}
