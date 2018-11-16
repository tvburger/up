package net.tvburger.up.runtimes.local;

import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.impl.UpEngineManagerImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.topology.UpRuntimeTopology;

public final class LocalUpInstance {

    private LocalUpRuntimeManager runtimeManager;

    public void init(UpRuntimeTopology runtimeDefinition) throws TopologyException, LifecycleException {
        if (runtimeDefinition.getEngineDefinitions().size() != 1) {
            throw new TopologyException("Must contain exactly 1 engine definition!");
        }
        runtimeManager = LocalUpRuntimeManager.Factory.create(runtimeDefinition.getEngineDefinitions().iterator().next());
        runtimeManager.init();
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
        return ((UpEngineManagerImpl) getEngine().getManager()).getIdentity();
    }

}
