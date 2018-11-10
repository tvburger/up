package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Set;

public interface Environment extends ManagedEntity<EnvironmentManager, EnvironmentInfo> {

    <T> Service<T> getService(Class<T> serviceType) throws AccessDeniedException, DeployException;

    Set<Service<?>> getServices() throws AccessDeniedException, DeployException;

    Set<Endpoint> getEndpoints(Specification endpointTechnology) throws AccessDeniedException, DeployException;

    UpRuntime getRuntime() throws AccessDeniedException;

}