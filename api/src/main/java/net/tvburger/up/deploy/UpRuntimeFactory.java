package net.tvburger.up.deploy;

import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.definitions.UpRuntimeDefinition;

public interface UpRuntimeFactory {

    UpClientTarget create(UpRuntimeDefinition runtimeDefinition) throws DeployException;

}
