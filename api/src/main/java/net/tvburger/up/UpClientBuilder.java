package net.tvburger.up;

import net.tvburger.up.identity.Identity;

public interface UpClientBuilder {

    String getEnvironment();

    UpClientBuilder withEnvironment(String environment);

    Identity getIdentity();

    UpClientBuilder withIdentity(Identity identity);

    UpClient build();

}
