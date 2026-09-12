package com.ianwijma.poweroffarts;

import com.ianwijma.poweroffarts.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class PowerOfFarts {

    public static void init() {
        Constants.LOG.info("Power Of Farts initializing on {} in a {} environment", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));
    }
}
