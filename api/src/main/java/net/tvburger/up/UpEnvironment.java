package net.tvburger.up;

import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.topology.UpApplicationTopology;
import net.tvburger.up.topology.UpEndpointDefinition;
import net.tvburger.up.topology.UpServiceDefinition;

import java.util.Map;
import java.util.Set;

public interface UpEnvironment extends ManagedEntity<UpEnvironment.Manager, UpEnvironment.Info> {

    interface Info extends ManagedEntity.Info {

        String getName();

        UpRuntimeInfo getRuntimeInfo();

    }

    interface Manager extends LogManager, LifecycleManager, ManagedEntity.Manager<Info> {

        void deploy(UpApplicationTopology deploymentDefinition) throws TopologyException;

        void deploy(UpServiceDefinition serviceDefinition) throws TopologyException;

        void deploy(UpEndpointDefinition endpointDefinition) throws TopologyException;

        UpApplicationTopology dump();

    }

    Set<Class<?>> listServiceTypes();

    <T> T lookupService(Class<T> serviceType);

    Set<UpService.Info<?>> listServices();

    <T> T getService(UpService.Info<T> serviceInfo) throws AccessDeniedException;

    <T> UpService.Manager<T> getServiceManager(UpService.Info<T> serviceInfo) throws AccessDeniedException;

    Map<Specification, Set<? extends UpEndpoint.Info>> listEndpoints();

    <I extends UpEndpoint.Info> UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException;

}