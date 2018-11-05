package net.tvburger.up.local.spi;

import net.tvburger.up.Environment;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.identity.Identity;
import net.tvburger.up.local.LocalUpInstance;
import net.tvburger.up.service.UpServiceContext;
import net.tvburger.up.spi.UpContextProvider;

public class LocalUpContextProvider implements UpContextProvider {

    private static final ThreadLocal<Identity> identity = ThreadLocal.withInitial(() -> Identity.ANONYMOUS);
    private static final ThreadLocal<Environment> environment = new ThreadLocal<>();

    public static void setIdentity(Identity identity) {
        LocalUpContextProvider.identity.set(identity);
    }

    public static void setEnvironment(Environment environment) {
        LocalUpContextProvider.environment.set(environment);
    }

    @Override
    public UpServiceContext getServiceContext() {
        return LocalUpServiceContext.get();
    }

    @Override
    public Identity getIdentity() {
        return identity.get();
    }

    @Override
    public Environment getEnvironment() {
        return environment.get();
    }

    @Override
    public UpRuntime getRuntime() {
        return LocalUpInstance.get().getRuntime();
    }

}
