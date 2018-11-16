package net.tvburger.up.runtime;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.behaviors.*;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.topology.UpEndpointDefinition;

import java.util.Set;

public interface UpEndpointTechnology<T, I extends UpEndpoint.Info> extends ManagedObject<UpEndpointTechnology.Manager<T>, UpEndpointTechnologyInfo> {

    interface Manager<T> extends Implementation, LogManager, LifecycleManager, ManagedObject.Manager<UpEndpointTechnologyInfo> {

        T deploy(UpEnvironment.Info environmentInfo, UpEndpointDefinition endpointDefinition) throws TopologyException, UpRuntimeException;

    }

    Specification getEngineRequirement();

    Set<I> listEndpoints(UpEnvironment.Info environmentInfo);

    UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException;

}
