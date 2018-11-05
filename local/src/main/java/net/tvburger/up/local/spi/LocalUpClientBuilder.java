package net.tvburger.up.local.spi;

import net.tvburger.up.Up;
import net.tvburger.up.UpClient;
import net.tvburger.up.UpClientBuilder;
import net.tvburger.up.identity.Identity;
import net.tvburger.up.local.LocalUpInstance;
import net.tvburger.up.local.LocalUpRuntime;

public class LocalUpClientBuilder implements UpClientBuilder {

    private String environment = Up.DEFAULT_ENVIRONMENT;
    private Identity identity = Identity.ANONYMOUS;

    @Override
    public String getEnvironment() {
        return environment;
    }

    @Override
    public UpClientBuilder withEnvironment(String environment) {
        if (environment == null || environment.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.environment = environment;
        return this;
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    @Override
    public UpClientBuilder withIdentity(Identity identity) {
        this.identity = identity;
        return this;
    }

    private boolean isValid() {
        return environment != null && !environment.isEmpty() &&
                identity != null;
    }

    @Override
    public UpClient build() {
        if (!isValid()) {
            throw new IllegalStateException();
        }
        LocalUpRuntime runtime = (LocalUpRuntime) LocalUpInstance.get().getRuntime();
        runtime.ensureExistsEnvironment(environment);
        UpClient client = runtime.getEnvironment(environment).getClient(identity);
        LocalUpContextProvider.setEnvironment(runtime.getEnvironment(environment));
        LocalUpContextProvider.setIdentity(identity);
        return client;
    }

}
