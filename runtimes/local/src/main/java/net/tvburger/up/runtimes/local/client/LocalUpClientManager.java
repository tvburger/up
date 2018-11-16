package net.tvburger.up.runtimes.local.client;

import net.tvburger.up.client.UpClient;

import java.io.IOException;

public final class LocalUpClientManager implements UpClient.Manager {

    private final UpClient.Info info;

    public LocalUpClientManager(UpClient.Info info) {
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
