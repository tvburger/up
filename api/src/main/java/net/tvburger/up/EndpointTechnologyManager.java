package net.tvburger.up;

import net.tvburger.up.behaviors.*;
import net.tvburger.up.topology.EndpointDefinition;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.security.AccessDeniedException;

public interface EndpointTechnologyManager<T> extends Implementation, LogManager, LifecycleManager, ManagedObject.Manager<EndpointTechnologyInfo<T>> {

    T deploy(EnvironmentInfo environmentInfo, EndpointDefinition endpointDefinition) throws DeployException, AccessDeniedException;

    Specification getEngineRequirement();

}
