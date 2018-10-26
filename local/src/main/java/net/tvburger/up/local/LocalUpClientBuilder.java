package net.tvburger.up.local;

import net.tvburger.up.UpClient;
import net.tvburger.up.UpClientBuilder;
import net.tvburger.up.impl.ProtocolLifecycleManagerProvider;

public class LocalUpClientBuilder implements UpClientBuilder {

    public static final String DEFAULT_ENVIRONMENT = "default";

    private String environment = DEFAULT_ENVIRONMENT;

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
    public boolean isValid() {
        return environment != null && !environment.isEmpty();
    }

    @Override
    public UpClient build() {
        if (!isValid()) {
            throw new IllegalStateException();
        }
        ProtocolLifecycleManagerProvider provider = new ProtocolLifecycleManagerProvider();
        UpClient upClient = new LocalUpClient(LocalEnvironmentManager.get(environment), provider);
        provider.init(upClient);
        return upClient;
    }

}
