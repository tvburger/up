package net.tvburger.up.util;

import net.tvburger.up.spi.UpClientBuilderFactory;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

public final class UpClientBuilderFactoryLoader {

    public static Set<UpClientBuilderFactory> load() {
        UpClientBuilderFactoryLoader loader = new UpClientBuilderFactoryLoader();
        loader.init();
        return loader.clientBuilderFactories;
    }

    private final Set<UpClientBuilderFactory> clientBuilderFactories = new LinkedHashSet<>();

    public void init() {
        ServiceLoader<UpClientBuilderFactory> serviceLoader = ServiceLoader.load(UpClientBuilderFactory.class);
        for (UpClientBuilderFactory factory : serviceLoader) {
            clientBuilderFactories.add(factory);
        }
    }

}
