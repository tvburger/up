package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.security.AccessDeniedException;

public interface Environment extends ManagedEntity<EnvironmentManager, EnvironmentInfo> {

    <T> Service<T> getService(Class<T> serviceType) throws AccessDeniedException, DeployException;

    <T> EndpointTechnology<T> getEndpointTechnology(EndpointTechnologyInfo<T> info) throws AccessDeniedException, DeployException;

}