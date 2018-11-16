package net.tvburger.up.infra;

import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.topology.UpRuntimeTopology;

public interface InfrastructureProvisioner {

    UpClientTarget provision(UpRuntimeTopology runtimeDefinition) throws UpRuntimeException;

}
