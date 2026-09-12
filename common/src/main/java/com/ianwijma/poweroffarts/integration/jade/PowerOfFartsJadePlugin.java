package com.ianwijma.poweroffarts.integration.jade;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.energy.SimpleEnergyBuffer;
import com.ianwijma.poweroffarts.machine.FartGeneratorBlockEntity;
import com.ianwijma.poweroffarts.machine.GasMachineBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(Constants.MOD_ID)
public class PowerOfFartsJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(GasServerDataProvider.INSTANCE, GasMachineBlockEntity.class);
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
}