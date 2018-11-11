package net.tvburger.up.runtime;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Set;

public interface UpEngine extends ManagedEntity<UpEngineManager, UpEngineInfo> {

    Set<EndpointTechnologyInfo<?>> getEndpointTechnologies() throws AccessDeniedException;

    <T> EndpointTechnology<T> getEndpointTechnology(EndpointTechnologyInfo<T> info) throws AccessDeniedException;

    @SuppressWarnings("unchecked")
    default <T> EndpointTechnology<T> getEndpointTechnology(Class<T> endpointType) throws AccessDeniedException, DeployException {
        for (EndpointTechnologyInfo<?> info : getEndpointTechnologies()) {
            if (info.getEndpointType().equals(endpointType)) {
                return getEndpointTechnology((EndpointTechnologyInfo<T>) info);
            }
        }
        throw new DeployException("No endpoint technology present for type: " + endpointType);
    }

    UpRuntime getRuntime();

}
