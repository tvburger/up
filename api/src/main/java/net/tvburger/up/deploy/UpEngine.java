package net.tvburger.up.deploy;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Set;

public interface UpEngine extends ManagedEntity<UpEngineManager, UpEngineInfo> {

    Set<EndpointTechnologyInfo<?>> getEndpointTechnologies() throws AccessDeniedException;

    <T> EndpointTechnology<T> getEndpointTechnology(EndpointTechnologyInfo<T> info) throws AccessDeniedException;

    UpRuntime getRuntime();

}
