package net.tvburger.up;

import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.deploy.*;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Map;
import java.util.Set;

public interface UpEnvironment extends ManagedEntity<UpEnvironment.Manager, UpEnvironment.Info> {

    interface Info extends ManagedEntity.Info {

        String getName();

        UpRuntimeInfo getRuntimeInfo();

    }

    interface Manager extends LogManager, LifecycleManager, ManagedEntity.Manager<Info> {

        boolean supportsPackageDefinitionType(Class<? extends UpPackageDefinition> packageDefinitionType);

        UpPackage.Manager deployPackage(UpPackageDefinition packageDefinition) throws DeployException;

        UpApplication.Manager createApplication(String name, UpPackage.Info packageInfo) throws DeployException;

        UpApplication.Manager deployApplication(UpApplicationDefinition applicationDefinition, UpPackage.Info packageInfo) throws DeployException;

        UpService.Manager<?> deployService(UpServiceDefinition serviceDefinition, UpApplication.Info applicationInfo) throws DeployException;

        UpEndpoint.Manager<?> deployEndpoint(UpEndpointDefinition endpointDefinition, UpApplication.Info applicationInfo) throws DeployException;

    }

    Set<Class<?>> listServiceTypes();

    <T> T lookupService(Class<T> serviceType);

    Set<UpService.Info<?>> listServices();

    <T> T getService(UpService.Info<T> serviceInfo) throws AccessDeniedException;

    <T> UpService.Manager<T> getServiceManager(UpService.Info<T> serviceInfo) throws AccessDeniedException;

    Map<Specification, Set<? extends UpEndpoint.Info>> listEndpoints();

    <I extends UpEndpoint.Info> UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException;

    Set<UpPackage.Info> listPackages();

    UpPackage getPackage(UpPackage.Info packageInfo) throws AccessDeniedException;

    Set<UpApplication.Info> listApplications();

    UpApplication getApplication(UpApplication.Info applicationInfo) throws AccessDeniedException;

}