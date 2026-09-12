package com.ianwijma.poweroffarts.fabric.client;

import java.util.IdentityHashMap;
import java.util.Map;

import com.ianwijma.poweroffarts.Constants;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin(Constants.MOD_ID)
public class PowerOfFartsJadeClient implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(GasTooltipProvider.INSTANCE, com.ianwijma.poweroffarts.machine.MachineBlock.class);
    }

    public static class GasTooltipProvider implements IBlockComponentProvider {

        public static final GasTooltipProvider INSTANCE = new GasTooltipProvider();

        @Override
        public net.minecraft.resources.Identifier getUid() {
            return net.minecraft.resources.Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gas_tooltip");
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            CompoundTag serverData = accessor.getServerData();
            if (serverData.contains("pofGas")) {
                CompoundTag gas = serverData.getCompoundOrEmpty("pofGas");
                tooltip.add(Component.translatable("tooltip.poweroffarts.jade.gas",
                        String.format("%.1f", gas.getDoubleOr("amount", 0.0)),
                        String.format("%.0f", gas.getDoubleOr("capacity", 0.0))).withStyle(ChatFormatting.GREEN));
            }
            if (serverData.contains("pofEnergy")) {
                CompoundTag energyTag = serverData.getCompoundOrEmpty("pofEnergy");
                tooltip.add(Component.translatable("tooltip.poweroffarts.jade.energy",
                        energyTag.getLongOr("amount", 0L), energyTag.getLongOr("capacity", 0L)).withStyle(ChatFormatting.YELLOW));
            }
        }
    }
}