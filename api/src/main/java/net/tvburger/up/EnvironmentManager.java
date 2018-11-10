package net.tvburger.up;

import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.topology.EndpointDefinition;
import net.tvburger.up.topology.ServiceDefinition;
import net.tvburger.up.topology.UpApplicationTopology;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.security.AccessDeniedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface EnvironmentManager extends LogManager, LifecycleManager, ManagedEntity.Manager<EnvironmentInfo> {

    void dump(OutputStream out) throws IOException;

    void restore(InputStream in) throws IOException;

    void deploy(UpApplicationTopology deploymentDefinition) throws AccessDeniedException, DeployException;

    void deploy(ServiceDefinition serviceDefinition) throws AccessDeniedException, DeployException;

    void deploy(EndpointDefinition endpointDefinition) throws AccessDeniedException, DeployException;

}
