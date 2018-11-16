package net.tvburger.up.runtime;

import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpRuntimeInfo;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Set;

public interface UpRuntime extends ManagedEntity<UpRuntime.Manager, UpRuntimeInfo> {

    interface Manager extends LifecycleManager, ManagedEntity.Manager<UpRuntimeInfo> {

        UpEnvironment createEnvironment(String environmentName) throws UpRuntimeException;

    }

    Set<UpEngine.Info> listEngines();

    UpEngine.Manager getEngineManager(UpEngine.Info engineInfo) throws AccessDeniedException;

    Set<String> listEnvironments();

    boolean hasEnvironment(String environmentName);

    UpEnvironment getEnvironment(String environmentName) throws AccessDeniedException;

    Set<UpEndpointTechnologyInfo> listEndpointTechnologies();

    UpEndpointTechnology.Manager<?> getEndpointTechnologyManager(UpEndpointTechnologyInfo endpointInfo) throws AccessDeniedException;

}
