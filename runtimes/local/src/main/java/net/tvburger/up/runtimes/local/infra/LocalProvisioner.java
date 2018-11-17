package net.tvburger.up.runtimes.local.infra;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientException;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.infra.InfrastructureProvisioner;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.runtimes.local.LocalInstance;
import net.tvburger.up.runtimes.local.client.LocalClientTarget;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.topology.UpEngineDefinition;
import net.tvburger.up.topology.UpRuntimeTopology;
import net.tvburger.up.util.Identities;
import net.tvburger.up.util.LocalJavaImplementation;

import java.util.HashMap;
import java.util.Map;

public final class LocalProvisioner implements InfrastructureProvisioner {

    private static final Map<UpEnvironment, LocalClientTarget> targets = new HashMap<>();

    public static UpEnvironment createEnvironment(Implementation... endpointImplementation) throws UpRuntimeException {
        try {
            UpEngineDefinition.Builder builder = new UpEngineDefinition.Builder()
                    .withImplementation(LocalJavaImplementation.get());
            if (endpointImplementation != null) {
                for (Implementation implementation : endpointImplementation) {
                    builder.withEndpointImplementation(implementation);
                }
            }
            LocalClientTarget target = (LocalClientTarget) new LocalProvisioner().provision(UpRuntimeTopology.Factory.create(builder.build()));
            UpClient client = UpClient.newBuilder(target).withIdentity(Identities.ANONYMOUS).withEnvironment("default").build();
            UpEnvironment environment = client.getEnvironment();
            targets.put(environment, target);
            return environment;
        } catch (AccessDeniedException | UpClientException cause) {
            throw new UpRuntimeException(cause);
        }
    }

    public static void destroyEnvironment(UpEnvironment environment) throws UpRuntimeException {
        try {
            if (!targets.containsKey(environment)) {
                throw new UpRuntimeException("No such environment found!");
            }
            UpRuntime.Manager manager = targets.remove(environment).getInstance().getRuntime().getManager();
            manager.stop();
            manager.destroy();
        } catch (AccessDeniedException | LifecycleException cause) {
            throw new UpRuntimeException(cause);
        }
    }

    public static void cleanUp(UpClientTarget target) throws UpRuntimeException {
        try {
            if (!(target instanceof LocalClientTarget)) {
                throw new UpRuntimeException("Invalid client target!");
            }
            LocalInstance localInstance = ((LocalClientTarget) target).getInstance();
            localInstance.getRuntime().getManager().stop();
            localInstance.getRuntime().getManager().destroy();
        } catch (AccessDeniedException | LifecycleException cause) {
            throw new UpRuntimeException(cause);
        }
    }

    @Override
    public UpClientTarget provision(UpRuntimeTopology runtimeDefinition) throws UpRuntimeException {
        try {
            LocalInstance instance = new LocalInstance();
            instance.init(runtimeDefinition);
            instance.getRuntime().getManager().start();
            return new LocalClientTarget(instance);
        } catch (AccessDeniedException | TopologyException | LifecycleException cause) {
            throw new UpRuntimeException(cause);
        }
    }

}
