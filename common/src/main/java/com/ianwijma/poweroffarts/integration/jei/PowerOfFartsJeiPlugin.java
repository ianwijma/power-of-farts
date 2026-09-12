package com.ianwijma.poweroffarts.integration.jei;

import com.ianwijma.poweroffarts.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class PowerOfFartsJeiPlugin implements IModPlugin {

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "jei");
    }
}
