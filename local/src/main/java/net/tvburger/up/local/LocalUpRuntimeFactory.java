package net.tvburger.up.local;

import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.definitions.UpRuntimeDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpRuntimeFactory;

public class LocalUpRuntimeFactory implements UpRuntimeFactory {

    @Override
    public UpClientTarget create(UpRuntimeDefinition runtimeDefinition) throws DeployException {
        LocalUpInstance instance = new LocalUpInstance();
        instance.init();
        return new LocalUpClientTarget(instance);
    }

}
