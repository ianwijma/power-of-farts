package com.ianwijma.poweroffarts.machine;

import net.minecraft.core.Direction;

import com.ianwijma.poweroffarts.gas.Gas;

public interface IGasAcceptor {

    boolean canAcceptGasFrom(Direction face);

    double insertGas(Gas gas, double maxAmount, Direction from, boolean simulate);

    default void onGasReceived(Direction face) {
    }
}
