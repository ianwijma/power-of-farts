package com.ianwijma.poweroffarts.platform;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.platform.services.IPlatformHelper;
import com.ianwijma.poweroffarts.platform.IPlatformNetwork;
import com.ianwijma.poweroffarts.platform.IPlatformRegistry;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IPlatformRegistry REGISTRY = load(IPlatformRegistry.class);
    public static final IPlatformNetwork NETWORK = load(IPlatformNetwork.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
