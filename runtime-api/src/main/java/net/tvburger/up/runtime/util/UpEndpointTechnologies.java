package net.tvburger.up.runtime.util;

import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.impl.ImplementationImpl;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.runtime.spi.UpEndpointTechnologyProvider;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.topology.TopologyException;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

public final class UpEndpointTechnologies {

    public static Map<Class<?>, UpEndpointTechnology<?, ?>> load() throws TopologyException {
        try {
            Map<Class<?>, UpEndpointTechnology<?, ?>> endpointTechnologies = new HashMap<>();
            for (UpEndpointTechnologyProvider provider : ServiceLoader.load(UpEndpointTechnologyProvider.class)) {
                Class<?> endpointType = provider.getEndpointType();
                if (!endpointTechnologies.containsKey(endpointType)) {
                    endpointTechnologies.put(endpointType, provider.getEndpointTechnology());
                }
            }
            return endpointTechnologies;
        } catch (UpRuntimeException cause) {
            throw new TopologyException("Failed to load EndpointTechnologies: " + cause.getMessage(), cause);
        }
    }

    public static Map<Class<?>, UpEndpointTechnology<?, ?>> load(Set<Implementation> technologyImplementations) throws TopologyException {
        try {
            Map<Class<?>, UpEndpointTechnology<?, ?>> allTechnologies = load();
            Map<Class<?>, UpEndpointTechnology<?, ?>> specifiedTechnologies = new HashMap<>();
            Map<Implementation, Class<?>> index = new HashMap<>();
            for (UpEndpointTechnology<?, ?> endpointTechnology : allTechnologies.values()) {
                UpEndpointTechnology.Manager<?> manager = endpointTechnology.getManager();
                index.put(ImplementationImpl.Factory.create(
                        manager.getSpecification(),
                        manager.getImplementationName(),
                        manager.getImplementationVersion()), endpointTechnology.getInfo().getEndpointType());
            }
            for (Implementation technologyImplementation : technologyImplementations) {
                if (index.containsKey(technologyImplementation)) {
                    Class<?> endpointType = index.get(technologyImplementation);
                    specifiedTechnologies.put(endpointType, allTechnologies.get(endpointType));
                } else {
                    throw new TopologyException("No such technology available: " + technologyImplementation);
                }
            }
            return specifiedTechnologies;
        } catch (AccessDeniedException cause) {
            throw new TopologyException("Failed to load EndpointTechnologies: " + cause.getMessage(), cause);
        }
    }

    private UpEndpointTechnologies() {
    }

}
