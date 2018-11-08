package net.tvburger.up.local;

import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.definitions.UpRuntimeDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpRuntimeFactory;
import net.tvburger.up.deploy.UpRuntimeManager;
import net.tvburger.up.local.impl.LocalUpClientTarget;
import net.tvburger.up.local.impl.LocalUpInstance;
import net.tvburger.up.security.AccessDeniedException;

public final class LocalUpRuntimeFactory implements UpRuntimeFactory {

    @Override
    public UpClientTarget create(UpRuntimeDefinition runtimeDefinition) throws DeployException {
        try {
            LocalUpInstance instance = new LocalUpInstance();
            instance.init(runtimeDefinition);
            instance.getRuntime().getManager().start();
            return new LocalUpClientTarget(instance);
        } catch (AccessDeniedException | LifecycleException cause) {
            throw new DeployException(cause);
        }
    }

}
