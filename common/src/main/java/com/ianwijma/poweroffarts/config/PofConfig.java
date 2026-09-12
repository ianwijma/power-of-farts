package com.ianwijma.poweroffarts.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.platform.Services;

public final class PofConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final AtomicBoolean LOADED = new AtomicBoolean(false);

    private static volatile PofConfig instance = new PofConfig(new JsonObject());

    public final double gasPerHunger;
    public final double playerGasCapacity;
    public final double digestionRatePerSecond;
    public final double fartCloudRadius;
    public final int fartCloudDurationSeconds;
    public final int nauseaSeconds;
    public final double gasBagCapacity;
    public final double gasTankCapacity;
    public final double depositorTransferRate;
    public final double pipeTransferRate;
    public final double generatorBurnRate;
    public final double generatorPowerOutput;

    private PofConfig(JsonObject json) {
        gasPerHunger = readDouble(json, "gasPerHunger", 10.0);
        playerGasCapacity = readDouble(json, "playerGasCapacity", 100.0);
        digestionRatePerSecond = readDouble(json, "digestionRatePerSecond", 0.5);
        fartCloudRadius = readDouble(json, "fartCloudRadius", 3.0);
        fartCloudDurationSeconds = (int) readDouble(json, "fartCloudDurationSeconds", 10);
        nauseaSeconds = (int) readDouble(json, "nauseaSeconds", 5);
        gasBagCapacity = readDouble(json, "gasBagCapacity", 500.0);
        gasTankCapacity = readDouble(json, "gasTankCapacity", 8000.0);
        depositorTransferRate = readDouble(json, "depositorTransferRate", 2.0);
        pipeTransferRate = readDouble(json, "pipeTransferRate", 5.0);
        generatorBurnRate = readDouble(json, "generatorBurnRate", 4.0);
        generatorPowerOutput = readDouble(json, "generatorPowerOutput", 20.0);
    }

    public static PofConfig get() {
        loadIfNeeded();
        return instance;
    }

    public static void load() {
        loadIfNeeded();
    }

    private static void loadIfNeeded() {
        if (!LOADED.compareAndSet(false, true)) {
            return;
        }
        Path file = Services.PLATFORM.getConfigDir().resolve(Constants.MOD_ID + ".json");
        try {
            JsonObject json = new JsonObject();
            if (Files.exists(file)) {
                json = GSON.fromJson(Files.readString(file), JsonObject.class);
                if (json == null) {
                    json = new JsonObject();
                }
            }
            PofConfig config = new PofConfig(json);
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(config.toJson()));
            instance = config;
        } catch (IOException e) {
            Constants.LOG.error("Failed to load config at {}, using defaults", file, e);
            instance = new PofConfig(new JsonObject());
        }
        Constants.LOG.info("Config loaded: gasPerHunger={}, playerGasCapacity={}", instance.gasPerHunger, instance.playerGasCapacity);
    }

    private static double readDouble(JsonObject json, String key, double fallback) {
        return json.has(key) ? json.get(key).getAsDouble() : fallback;
    }

    private JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("gasPerHunger", gasPerHunger);
        json.addProperty("playerGasCapacity", playerGasCapacity);
        json.addProperty("digestionRatePerSecond", digestionRatePerSecond);
        json.addProperty("fartCloudRadius", fartCloudRadius);
        json.addProperty("fartCloudDurationSeconds", fartCloudDurationSeconds);
        json.addProperty("nauseaSeconds", nauseaSeconds);
        json.addProperty("gasBagCapacity", gasBagCapacity);
        json.addProperty("gasTankCapacity", gasTankCapacity);
        json.addProperty("depositorTransferRate", depositorTransferRate);
        json.addProperty("pipeTransferRate", pipeTransferRate);
        json.addProperty("generatorBurnRate", generatorBurnRate);
        json.addProperty("generatorPowerOutput", generatorPowerOutput);
        return json;
    }
}
