package net.tvburger.up;

import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEndpointDefinition;
import net.tvburger.up.deploy.UpServiceDefinition;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Map;
import java.util.Set;

public interface UpApplication extends ManagedEntity<UpApplication.Manager, UpApplication.Info> {

    interface Manager extends LifecycleManager, LogManager, ManagedEntity.Manager<Info> {

        UpService.Manager<?> deployService(UpServiceDefinition serviceDefinition) throws DeployException;

        UpEndpoint.Manager<?> deployEndpoint(UpEndpointDefinition endpointDefinition) throws DeployException;

    }

    interface Info extends ManagedEntity.Info {

        String getName();

        UpPackage.Info getPackageInfo();

        UpEnvironment.Info getEnvironmentInfo();

    }

    Set<UpService.Info<?>> listServices();

    <T> T getService(UpService.Info<T> serviceInfo) throws AccessDeniedException;

    <T> UpService.Manager<T> getServiceManager(UpService.Info<T> serviceInfo) throws AccessDeniedException;

    Map<Specification, Set<? extends UpEndpoint.Info>> listEndpoints();

    <I extends UpEndpoint.Info> UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException;

}
