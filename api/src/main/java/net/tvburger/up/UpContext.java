package net.tvburger.up;

import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.identity.Identity;
import net.tvburger.up.service.UpServiceContext;
import net.tvburger.up.spi.UpContextProvider;

import java.util.ServiceLoader;

public final class UpContext {

    private static final UpContextProvider contextProvider = ServiceLoader.load(UpContextProvider.class).iterator().next();

    public static UpServiceContext getServiceContext() {
        return contextProvider.getServiceContext();
    }

    public static Identity getIdentity() {
        return contextProvider.getIdentity();
    }

    public static Environment getEnvironment() {
        return contextProvider.getEnvironment();
    }

    public static UpRuntime getRuntime() {
        return contextProvider.getRuntime();
    }

}
