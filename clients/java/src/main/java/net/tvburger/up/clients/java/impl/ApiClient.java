package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;

public final class ApiClient extends ApiRequester implements UpClient {

    private final Identification identification;
    private final UpClient.Manager manager;

    ApiClient(ApiRequester requester, Identity identity, UpClient.Manager manager) {
        super(requester);
        this.identification = identity;
        this.manager = manager;
    }

    @Override
    public UpEnvironment getEnvironment() {
        return new ClientEnvironment(this);
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public Manager getManager() {
        return manager;
    }

    @Override
    public Info getInfo() {
        return manager.getInfo();
    }

}
