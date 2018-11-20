package net.tvburger.up.infra;

import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.runtime.UpRuntimeException;

public interface InfrastructureProvisioner {

    UpClientTarget provision(UpRuntimeTopology runtimeDefinition) throws UpRuntimeException;

}
