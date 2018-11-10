package net.tvburger.up.runtime;

import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.topology.UpRuntimeTopology;

public interface UpRuntimeFactory {

    UpClientTarget create(UpRuntimeTopology runtimeDefinition) throws DeployException;

}
