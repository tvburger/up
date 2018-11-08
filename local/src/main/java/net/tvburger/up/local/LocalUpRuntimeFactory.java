package net.tvburger.up.local;

import net.tvburger.up.Environment;
import net.tvburger.up.Up;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.definitions.UpEngineDefinition;
import net.tvburger.up.definitions.UpRuntimeDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpRuntimeFactory;
import net.tvburger.up.deploy.UpRuntimeManager;
import net.tvburger.up.local.impl.LocalUpClientTarget;
import net.tvburger.up.local.impl.LocalUpInstance;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.util.Identities;

import java.util.HashMap;
import java.util.Map;

public final class LocalUpRuntimeFactory implements UpRuntimeFactory {

    private static final Map<Environment, LocalUpClientTarget> targets = new HashMap<>();

    public static Environment createEnvironment(Implementation... endpointImplementation) throws DeployException {
        try {
            UpEngineDefinition.Builder builder = new UpEngineDefinition.Builder()
                    .withImplementation(LocalJavaImplementation.get());
            if (endpointImplementation != null) {
                for (Implementation implementation : endpointImplementation) {
                    builder.withEndpointImplementation(implementation);
                }
            }
            LocalUpClientTarget target = (LocalUpClientTarget) new LocalUpRuntimeFactory().create(UpRuntimeDefinition.Factory.create(builder.build()));
            UpClient client = Up.createClientBuilder(target).withIdentity(Identities.ANONYMOUS).withEnvironment("default").build();
            Environment environment = client.getEnvironment();
            targets.put(environment, target);
            return environment;
        } catch (AccessDeniedException cause) {
            throw new DeployException(cause);
        }
    }

    public static void destroyEnvironment(Environment environment) throws DeployException {
        try {
            if (!targets.containsKey(environment)) {
                throw new DeployException("No such environment found!");
            }
            UpRuntimeManager manager = targets.remove(environment).getInstance().getRuntime().getManager();
            manager.stop();
            manager.destroy();
        } catch (AccessDeniedException | LifecycleException cause) {
            throw new DeployException(cause);
        }
    }

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
