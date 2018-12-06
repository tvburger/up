package net.tvburger.up.runtimes.local.impl.client;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.runtimes.local.client.LocalClientTarget;
import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;

public final class LocalClient implements UpClient {

    private final LocalClientTarget target;
    private final LocalClientManager manager;
    private final Identity identity;
    private final UpEnvironment environment;

    public LocalClient(LocalClientTarget target, LocalClientManager manager, Identity identity, UpEnvironment environment) {
        this.target = target;
        this.manager = manager;
        this.identity = identity;
        this.environment = environment;
    }

    @Override
    public LocalClientManager getManager() {
        return manager;
    }

    public UpClient.Info getInfo() {
        return manager.getInfo();
    }

    @Override
    public Identification getIdentification() {
        return Identities.getSafeIdentification(identity);
    }

    @Override
    public UpEnvironment getEnvironment() {
        return environment;
    }

}
