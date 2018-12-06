package net.tvburger.up.runtimes.local.impl.client;

import net.tvburger.up.client.UpClient;

import java.io.IOException;

public final class LocalClientManager implements UpClient.Manager {

    private final UpClient.Info info;

    public LocalClientManager(UpClient.Info info) {
        this.info = info;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public UpClient.Info getInfo() {
        return info;
    }

}
