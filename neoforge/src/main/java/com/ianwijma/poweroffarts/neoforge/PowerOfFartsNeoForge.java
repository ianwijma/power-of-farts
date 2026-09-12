package com.ianwijma.poweroffarts.neoforge;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.PofRegistries;
import com.ianwijma.poweroffarts.PowerOfFarts;
import com.ianwijma.poweroffarts.config.PofConfig;
import com.ianwijma.poweroffarts.platform.Services;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class PowerOfFartsNeoForge {

    public PowerOfFartsNeoForge(IEventBus eventBus) {
        NeoForgeRegistryService.init(eventBus);
        PofRegistries.init();
        Services.REGISTRY.commit();
        PofConfig.load();
        PofCreativeTabs.touch();
        NeoForge.EVENT_BUS.addListener(PofNeoForgeEvents::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(PofNeoForgeEvents::onRegisterCommands);
        Constants.LOG.info("Hello NeoForge world!");
        PowerOfFarts.init();
    }
}
