package net.tvburger.up;

import net.tvburger.up.identity.Identity;
import net.tvburger.up.spi.UpClientBuilderFactory;

import java.util.ServiceLoader;

public final class Up {

    public static final String DEFAULT_ENVIRONMENT = "default";

    private static final UpClientBuilderFactory clientBuilderFactory = ServiceLoader.load(UpClientBuilderFactory.class).iterator().next();

    public static UpClientBuilder createClientBuilder() {
        return clientBuilderFactory.createClientBuilder();
    }

    public static UpClient createClient() {
        return createClient(Identity.ANONYMOUS);
    }

    public static UpClient createClient(Identity identity) {
        return createClient(DEFAULT_ENVIRONMENT, identity);
    }

    public static UpClient createClient(String environmentName, Identity identity) {
        return createClientBuilder().withEnvironment(environmentName).withIdentity(identity).build();
    }

}
