package net.tvburger.up.spi;

import net.tvburger.up.Environment;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.identity.Identity;
import net.tvburger.up.service.UpServiceContext;

public interface UpContextProvider {

    UpServiceContext getServiceContext();

    Identity getIdentity();

    Environment getEnvironment();

    UpRuntime getRuntime();

}
