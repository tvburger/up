package net.tvburger.up.runtime;

import net.tvburger.up.Environment;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Set;

public interface UpRuntimeManager extends LifecycleManager, ManagedEntity.Manager<UpRuntimeInfo> {

    Set<Environment> getEnvironments();

    Environment createEnvironment(String environmentName) throws AccessDeniedException, DeployException;

}
