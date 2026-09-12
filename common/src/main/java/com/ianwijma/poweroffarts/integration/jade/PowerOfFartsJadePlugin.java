package com.ianwijma.poweroffarts.integration.jade;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.energy.SimpleEnergyBuffer;
import com.ianwijma.poweroffarts.machine.FartGeneratorBlockEntity;
import com.ianwijma.poweroffarts.machine.GasMachineBlockEntity;
import com.ianwijma.poweroffarts.machine.PofBlocks;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin(Constants.MOD_ID)
public class PowerOfFartsJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(GasServerDataProvider.INSTANCE, GasMachineBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(GasTooltipProvider.INSTANCE, PofBlocks.DEPOSITOR.get().getClass());
        registration.registerBlockComponent(GasTooltipProvider.INSTANCE, PofBlocks.PIPE.get().getClass());
        registration.registerBlockComponent(GasTooltipProvider.INSTANCE, PofBlocks.GAS_TANK.get().getClass());
        registration.registerBlockComponent(GasTooltipProvider.INSTANCE, PofBlocks.GENERATOR.get().getClass());
    }

    public static class GasServerDataProvider implements IServerDataProvider<BlockAccessor> {

        public static final GasServerDataProvider INSTANCE = new GasServerDataProvider();

        @Override
        public Identifier getUid() {
            return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gas_server_data");
        }

        @Override
        public void appendServerData(CompoundTag data, BlockAccessor accessor) {
            if (accessor.getTarget() instanceof GasMachineBlockEntity machine) {
                CompoundTag gas = new CompoundTag();
                gas.putDouble("amount", machine.getTank().getAmount());
                gas.putDouble("capacity", machine.getTank().getCapacity());
                data.put("pofGas", gas);
                if (machine instanceof FartGeneratorBlockEntity generator) {
                    SimpleEnergyBuffer energy = generator.getEnergy();
                    CompoundTag energyTag = new CompoundTag();
                    energyTag.putLong("amount", energy.getEnergyStored());
                    energyTag.putLong("capacity", energy.getMaxEnergyStored());
                    data.put("pofEnergy", energyTag);
                }
            }
        }
    }

    public static class GasTooltipProvider implements IBlockComponentProvider {

        public static final GasTooltipProvider INSTANCE = new GasTooltipProvider();

        @Override
        public Identifier getUid() {
            return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gas_tooltip");
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
