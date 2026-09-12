package com.ianwijma.poweroffarts.client;

public final class ClientGasData {

    private static volatile double storedGas;
    private static volatile double pendingGas;

    public static double getStoredGas() {
        return storedGas;
    }

    public static double getPendingGas() {
        return pendingGas;
    }

    public static void update(double storedGas, double pendingGas) {
        ClientGasData.storedGas = storedGas;
        ClientGasData.pendingGas = pendingGas;
    }

    private ClientGasData() {
    }
}
