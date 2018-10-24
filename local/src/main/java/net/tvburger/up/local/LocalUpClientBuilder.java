package net.tvburger.up.local;

import net.tvburger.up.UpClient;
import net.tvburger.up.UpClientBuilder;

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
        return new LocalUpClient(LocalEnvironmentManager.get(environment));
    }

}
