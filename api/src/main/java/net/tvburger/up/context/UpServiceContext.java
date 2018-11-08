package net.tvburger.up.context;

import net.tvburger.up.Environment;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.Identity;

public interface UpServiceContext {

    ServiceInfo<?> getInfo();

    Identity getIdentity();

    Environment getEnvironment();

    Locality getLocality();

    UpEngine getEngine();

}
