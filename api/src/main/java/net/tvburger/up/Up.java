package net.tvburger.up;

import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.context.UpContext;
import net.tvburger.up.logger.UpLogger;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.spi.UpClientBuilderFactory;
import net.tvburger.up.spi.UpContextProvider;
import net.tvburger.up.spi.UpLoggerProvider;
import net.tvburger.up.util.UpClientBuilderFactoryLoader;

import java.util.ServiceLoader;
import java.util.Set;

public final class Up {

    private static final Set<UpClientBuilderFactory> clientBuilderFactories = UpClientBuilderFactoryLoader.load();
    private static final UpContextProvider contextProvider = ServiceLoader.load(UpContextProvider.class).iterator().next();
    private static final UpLoggerProvider loggerProvider = ServiceLoader.load(UpLoggerProvider.class).iterator().next();

    /**
     * Creates a new ClientBuilder for the specified target.
     *
     * @param target
     * @return
     * @throws DeployException
     */
    public static UpClientBuilder createClientBuilder(UpClientTarget target) throws DeployException {
        for (UpClientBuilderFactory factory : clientBuilderFactories) {
            if (factory.supportsTarget(target)) {
                return factory.createClientBuilder(target);
            }
        }
        throw new DeployException("Unsupported target: " + target);
    }

    /**
     * Returns null if called from outside an Engine (e.g. from a Client)
     *
     * @return
     */
    public static UpContext getContext() {
        return contextProvider.getContext();
    }

    /**
     * Only used for internal implementation of an Engine
     *
     * @param context
     */
    public static void setContext(UpContext context) {
        contextProvider.setContext(context);
    }

    public static UpLogger getLogger(String loggerName) {
        return loggerProvider.getLogger(loggerName);
    }

}
