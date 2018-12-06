package net.tvburger.up.runtimes.local;

import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.infra.UpRuntimeTopology;
import net.tvburger.up.infra.impl.UpEngineManagerImpl;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtimes.local.impl.LocalRuntimeManager;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;

public final class LocalInstance {

    private LocalRuntimeManager runtimeManager;

    public void init(UpRuntimeTopology runtimeDefinition) throws DeployException, LifecycleException {
        if (runtimeDefinition.getEngineDefinitions().size() != 1) {
            throw new DeployException("Must contain exactly 1 engine definition!");
        }
        runtimeManager = LocalRuntimeManager.Factory.create(runtimeDefinition.getEngineDefinitions().iterator().next());
        runtimeManager.init();
    }

    public UpRuntime getRuntime() {
        if (runtimeManager == null) {
            throw new IllegalStateException();
        }
        return runtimeManager.getRuntime();
    }

    public UpEngine getEngine() {
        if (runtimeManager == null) {
            throw new IllegalStateException();
        }
        return runtimeManager.getEngine();
    }

    public Identity getEngineIdentity() throws AccessDeniedException {
        if (runtimeManager == null) {
            throw new IllegalStateException();
        }
        return ((UpEngineManagerImpl) getEngine().getManager()).getIdentity();
    }

}
