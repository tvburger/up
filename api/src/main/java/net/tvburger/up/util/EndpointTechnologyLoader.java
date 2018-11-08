package net.tvburger.up.util;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.EndpointTechnologyManager;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.impl.ImplementationImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.spi.EndpointTechnologyProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

public final class EndpointTechnologyLoader {

    public static Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> load(UpEngine engine, Identity engineIdentity) throws DeployException {
        EndpointTechnologyLoader loader = new EndpointTechnologyLoader(engine, engineIdentity);
        loader.load();
        return loader.endpointTechnologies;
    }

    public static Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> load(UpEngine engine, Identity engineIdentity, Set<Implementation> endpointImplementations) throws DeployException {
        try {
            EndpointTechnologyLoader loader = new EndpointTechnologyLoader(engine, engineIdentity);
            loader.load();
            Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> endpointTechnologies = new HashMap<>();
            Map<Implementation, EndpointTechnology<?>> index = new HashMap<>();
            for (EndpointTechnology<?> endpointTechnology : loader.endpointTechnologies.values()) {
                EndpointTechnologyManager<?> manager = endpointTechnology.getManager();
                index.put(ImplementationImpl.Factory.create(
                        manager.getSpecification(),
                        manager.getImplementationName(),
                        manager.getImplementationVersion()), endpointTechnology);
            }
            for (Implementation implementation : endpointImplementations) {
                if (index.containsKey(implementation)) {
                    EndpointTechnology<?> endpointTechnology = index.get(implementation);
                    endpointTechnologies.put(endpointTechnology.getInfo(), loader.endpointTechnologies.get(endpointTechnology.getInfo()));
                } else {
                    throw new DeployException("No such technology available: " + implementation);
                }
            }
            return endpointTechnologies;
        } catch (AccessDeniedException cause) {
            throw new DeployException(cause);
        }
    }

    private final Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> endpointTechnologies = new HashMap<>();
    private final UpEngine engine;
    private final Identity engineIdentity;

    public EndpointTechnologyLoader(UpEngine engine, Identity engineIdentity) {
        this.engine = engine;
        this.engineIdentity = engineIdentity;
    }

    public void load() throws DeployException {
        ServiceLoader<EndpointTechnologyProvider> serviceLoader = ServiceLoader.load(EndpointTechnologyProvider.class);
        for (EndpointTechnologyProvider provider : serviceLoader) {
            EndpointTechnology<?> endpointTechnology = provider.getEndpointTechnology(engine, engineIdentity);
            endpointTechnologies.put(endpointTechnology.getInfo(), endpointTechnology);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> EndpointTechnology<T> getEndpointTechnology(EndpointTechnologyInfo<T> info) {
        EndpointTechnology<?> endpointTechnology = endpointTechnologies.get(info);
        if (endpointTechnology != null
                && endpointTechnology.getInfo().getEndpointType().equals(info.getEndpointType())) {
            return (EndpointTechnology<T>) endpointTechnologies.get(info);
        }
        return null;
    }

    public Set<EndpointTechnologyInfo<?>> getEndpointTechnologies() {
        return endpointTechnologies.keySet();
    }

}
