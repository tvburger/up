package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.definition.UpRuntimeDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;

public final class LocalUpInstance {

    private LocalUpRuntimeManager runtimeManager;

    public void init(UpRuntimeDefinition runtimeDefinition) throws DeployException {
        if (runtimeDefinition.getEngineDefinitions().size() != 1) {
            throw new DeployException("Must contain exactly 1 engine definition!");
        }
        runtimeManager = LocalUpRuntimeManager.Factory.create(runtimeDefinition.getEngineDefinitions().iterator().next());
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

    public Identity getEngineIdentity() throws AccessDeniedException {
        return ((LocalUpEngineManager) getEngine().getManager()).getIdentity();
    }

}
