package net.tvburger.up;

import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.definition.EndpointDefinition;
import net.tvburger.up.definition.ServiceDefinition;
import net.tvburger.up.definition.UpDeploymentDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.security.AccessDeniedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface EnvironmentManager extends LogManager, LifecycleManager, ManagedEntity.Manager<EnvironmentInfo> {

    void dump(OutputStream out) throws IOException;

    void restore(InputStream in) throws IOException;

    void deploy(UpDeploymentDefinition deploymentDefinition) throws AccessDeniedException, DeployException;

    void deploy(ServiceDefinition serviceDefinition) throws AccessDeniedException, DeployException;

    void deploy(EndpointDefinition endpointDefinition) throws AccessDeniedException, DeployException;

}
