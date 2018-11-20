package net.tvburger.up.runtime;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.behaviors.*;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEndpointDefinition;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Set;

public interface UpEndpointTechnology<I extends UpEndpoint.Info> extends ManagedObject<UpEndpointTechnology.Manager<I>, UpEndpointTechnologyInfo> {

    interface Manager<I extends UpEndpoint.Info> extends Implementation, LogManager, LifecycleManager, ManagedObject.Manager<UpEndpointTechnologyInfo> {

        UpEndpoint.Manager<I> deployEndpoint(UpEndpointDefinition endpointDefinition, UpApplication application) throws DeployException;

    }

    Specification getEngineRequirement();

    Set<I> listEndpoints(UpEnvironment.Info environmentInfo);

    UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException;

}
