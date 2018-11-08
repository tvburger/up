package net.tvburger.up;

import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.definitions.EndpointDefinition;
import net.tvburger.up.definitions.ServiceDefinition;
import net.tvburger.up.definitions.UpDeploymentDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.security.AccessDeniedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface EnvironmentManager extends LogManager, LifecycleManager, ManagedEntity.Manager<EnvironmentInfo> {

    void dump(OutputStream out) throws IOException;

    void restore(InputStream in) throws IOException;

    void deploy(UpDeploymentDefinition deploymentDefinition) throws AccessDeniedException, DeployException;

    void deploy(ServiceDefinition serviceDefinition, Class<?> serviceClass) throws AccessDeniedException, DeployException;

    void deploy(EndpointDefinition endpointDefinition, Class<?> serviceClass) throws AccessDeniedException, DeployException;

}
