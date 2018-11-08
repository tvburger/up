package net.tvburger.up.deploy;

import net.tvburger.up.Environment;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Set;

public interface UpRuntime extends ManagedEntity<UpRuntimeManager, UpRuntimeInfo> {

    Set<UpEngine> getEngines() throws AccessDeniedException;

    boolean hasEnvironment(String environmentName) throws AccessDeniedException;

    Environment getEnvironment(String environmentName) throws AccessDeniedException;

}
