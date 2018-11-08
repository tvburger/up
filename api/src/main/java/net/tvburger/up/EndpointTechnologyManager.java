package net.tvburger.up;

import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedObject;
import net.tvburger.up.definitions.EndpointDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.security.AccessDeniedException;

public interface EndpointTechnologyManager<T> extends Implementation, LogManager, LifecycleManager, ManagedObject.Manager<EndpointTechnologyInfo<T>> {

    void deploy(String environmentName, EndpointDefinition endpointDefinition, Class<?> serviceClass) throws DeployException, AccessDeniedException;

}
