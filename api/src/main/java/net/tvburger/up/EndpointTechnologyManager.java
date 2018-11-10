package net.tvburger.up;

import net.tvburger.up.behaviors.*;
import net.tvburger.up.definition.EndpointDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.security.AccessDeniedException;

public interface EndpointTechnologyManager<T> extends Implementation, LogManager, LifecycleManager, ManagedObject.Manager<EndpointTechnologyInfo<T>> {

    void deploy(EnvironmentInfo environmentInfo, EndpointDefinition endpointDefinition) throws DeployException, AccessDeniedException;

    Specification getEngineRequirement();

}
