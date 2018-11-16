package net.tvburger.up.runtimes.local.client;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;

public final class LocalUpClient implements UpClient {

    private final LocalUpClientTarget target;
    private final LocalUpClientManager manager;
    private final Identity identity;
    private final UpEnvironment environment;

    public LocalUpClient(LocalUpClientTarget target, LocalUpClientManager manager, Identity identity, UpEnvironment environment) {
        this.target = target;
        this.manager = manager;
        this.identity = identity;
        this.environment = environment;
    }

    @Override
    public LocalUpClientManager getManager() {
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
