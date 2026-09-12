package com.ianwijma.poweroffarts.neoforge;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.PowerOfFarts;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class PowerOfFartsNeoForge {

    public PowerOfFartsNeoForge(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        PowerOfFarts.init();
    }
}
