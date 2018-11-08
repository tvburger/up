package net.tvburger.up;

import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.UpServiceContext;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.spi.UpClientBuilderFactory;
import net.tvburger.up.spi.UpContextProvider;
import net.tvburger.up.util.UpClientBuilderFactoryLoader;

import java.util.ServiceLoader;
import java.util.Set;

public final class Up {

    private static final Set<UpClientBuilderFactory> clientBuilderFactories = UpClientBuilderFactoryLoader.load();
    private static final UpContextProvider contextProvider = ServiceLoader.load(UpContextProvider.class).iterator().next();

    public static UpClientBuilder createClientBuilder(UpClientTarget target) throws DeployException {
        for (UpClientBuilderFactory factory : clientBuilderFactories) {
            if (factory.supportsTarget(target)) {
                return factory.createClientBuilder(target);
            }
        }
        throw new DeployException("Unsupported target: " + target);
    }

    public static UpServiceContext getServiceContext() {
        return contextProvider.getServiceContext();
    }

    public static CallerInfo getCallerInfo() {
        return contextProvider.getCallerInfo();
    }

}
