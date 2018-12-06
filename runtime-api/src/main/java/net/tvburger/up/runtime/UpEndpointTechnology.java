package net.tvburger.up.runtime;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.*;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEndpointDefinition;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Map;
import java.util.Set;

public interface UpEndpointTechnology<I extends UpEndpoint.Info> extends ManagedObject<UpEndpointTechnology.Manager<I>, UpEndpointTechnologyInfo> {

    interface Manager<I extends UpEndpoint.Info> extends Implementation, LogManager, LifecycleManager, ManagedObject.Manager<UpEndpointTechnologyInfo> {

        UpEndpoint.Manager<I> deployEndpoint(UpEndpointDefinition endpointDefinition, UpApplication application, UpPackage upPackage) throws DeployException;

    }

    Specification getEngineRequirement();

    Map<UpApplication.Info, Set<I>> listEndpoints(UpEnvironment.Info environmentInfo);

    UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException;

}
