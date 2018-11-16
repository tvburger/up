package net.tvburger.up.client.util;

import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientException;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.client.spi.UpClientBuilderFactory;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

public final class UpClientBuilderFactories {

    public static Set<UpClientBuilderFactory> load() {
        Set<UpClientBuilderFactory> factories = new LinkedHashSet<>();
        ServiceLoader<UpClientBuilderFactory> serviceLoader = ServiceLoader.load(UpClientBuilderFactory.class);
        for (UpClientBuilderFactory factory : serviceLoader) {
            factories.add(factory);
        }
        return factories;
    }

    public static UpClientBuilderFactory get(UpClientTarget target) throws UpClientException {
        for (UpClientBuilderFactory factory : load()) {
            if (factory.supportsTarget(target)) {
                return factory;
            }
        }
        throw new UpClientException("Unsupported target: " + target);
    }

    public static UpClientBuilder create(UpClientTarget target) throws UpClientException {
        return get(target).createClientBuilder(target);
    }

    private UpClientBuilderFactories() {
    }

}
