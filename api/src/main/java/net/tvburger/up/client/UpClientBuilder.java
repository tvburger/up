package net.tvburger.up.client;

import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;

public interface UpClientBuilder {

    String getEnvironment();

    UpClientBuilder withEnvironment(String environment);

    Identity getIdentity();

    UpClientBuilder withIdentity(Identity identity);

    UpClient build() throws AccessDeniedException, DeployException;

}
