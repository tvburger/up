package net.tvburger.up;

import net.tvburger.up.admin.EnvironmentManager;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.identity.Entity;
import net.tvburger.up.identity.Identity;

public interface Environment extends Entity {

    EnvironmentManager getManager();

    EnvironmentInfo getInfo();

    UpClient getClient(Identity identity);

    UpRuntime getRuntime();

}
