package net.tvburger.up.local;

import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;

public final class LocalUpInstance {

    private LocalUpRuntimeManager runtimeManager;

    public void init() throws DeployException {
        try {
            runtimeManager = LocalUpRuntimeManager.Factory.create();
        } catch (LifecycleException cause) {
            throw new DeployException(cause);
        }
    }

    public UpRuntime getRuntime() {
        return runtimeManager.getRuntime();
    }

    public UpEngine getEngine() {
        return runtimeManager.getEngine();
    }

    public LocalEnvironmentManager getLocalEnvironmentManager(String environmentName) throws AccessDeniedException {
        return (LocalEnvironmentManager) getRuntime().getEnvironment(environmentName).getManager();
    }

}
