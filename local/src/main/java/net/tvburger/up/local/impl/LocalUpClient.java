package net.tvburger.up.local.impl;

import net.tvburger.up.Environment;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientInfo;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;

public final class LocalUpClient implements UpClient {

    private final LocalUpClientTarget target;
    private final LocalUpClientManager manager;
    private final Identity identity;

    public LocalUpClient(LocalUpClientTarget target, LocalUpClientManager manager, Identity identity) {
        this.target = target;
        this.manager = manager;
        this.identity = identity;
    }

    @Override
    public LocalUpClientManager getManager() {
        return manager;
    }

    public UpClientInfo getInfo() {
        return manager.getInfo();
    }

    @Override
    public Environment getEnvironment() throws AccessDeniedException {
        return target.getInstance().getRuntime().getEnvironment(manager.getInfo().getEnvironmentInfo().getName());
    }

    @Override
    public Identification getIdentification() {
        return Identities.getSafeIdentification(identity);
    }

}
