package net.tvburger.up.local;

import net.tvburger.up.Environment;
import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.UpClient;
import net.tvburger.up.admin.EnvironmentManager;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.identity.Identity;

public class LocalEnvironment implements Environment {

    private final LocalEnvironmentManager manager;

    public LocalEnvironment(LocalEnvironmentManager manager) {
        this.manager = manager;
    }

    @Override
    public EnvironmentManager getManager() {
        return manager;
    }

    @Override
    public EnvironmentInfo getInfo() {
        return manager.getEnvironmentInfo();
    }

    @Override
    public UpClient getClient(Identity identity) {
        return new LocalUpClient(manager, LocalUpInstance.get().getProvider(), identity);
    }

    @Override
    public UpRuntime getRuntime() {
        return LocalUpInstance.get().getRuntime();
    }

    @Override
    public Identity getIdentity() {
        return Identity.ANONYMOUS;
    }

}
