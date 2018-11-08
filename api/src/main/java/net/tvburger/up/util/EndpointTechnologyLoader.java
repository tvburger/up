package net.tvburger.up.util;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.EndpointTechnologyManager;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.spi.EndpointTechnologyProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

public final class EndpointTechnologyLoader {

    public static Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> load(UpEngine engine) throws DeployException {
        EndpointTechnologyLoader loader = new EndpointTechnologyLoader(engine);
        loader.init();
        return loader.endpointTechnologies;
    }

    private final Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> endpointTechnologies = new HashMap<>();
    private final UpEngine engine;

    public EndpointTechnologyLoader(UpEngine engine) {
        this.engine = engine;
    }

    public void init() throws DeployException {
        ServiceLoader<EndpointTechnologyProvider> serviceLoader = ServiceLoader.load(EndpointTechnologyProvider.class);
        for (EndpointTechnologyProvider provider : serviceLoader) {
            addEndpointTechnology(provider.getEndpointTechnology(engine));
        }
    }

    private void addEndpointTechnology(EndpointTechnology<?> endpointTechnology) throws DeployException {
        try {
            EndpointTechnologyManager<?> manager = endpointTechnology.getManager();
            manager.init();
            manager.start();
            endpointTechnologies.put(endpointTechnology.getInfo(), endpointTechnology);
        } catch (LifecycleException | AccessDeniedException cause) {
            throw new DeployException("Failed to load " + endpointTechnology.getInfo() + ": " + cause.getMessage(), cause);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> EndpointTechnology<T> getEndpointTechnology(EndpointTechnologyInfo<T> info) {
        EndpointTechnology<?> endpointTechnology = endpointTechnologies.get(info);
        if (endpointTechnology != null
                && endpointTechnology.getInfo().getEndpointManagerType().equals(info.getEndpointManagerType())) {
            return (EndpointTechnology<T>) endpointTechnologies.get(info);
        }
        return null;
    }

    public Set<EndpointTechnologyInfo<?>> getEndpointTechnologies() {
        return endpointTechnologies.keySet();
    }

}
