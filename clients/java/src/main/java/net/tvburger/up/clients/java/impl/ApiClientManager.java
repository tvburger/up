package net.tvburger.up.clients.java.impl;

import net.tvburger.up.client.UpClient;

import javax.ws.rs.client.Client;
import java.io.IOException;

public final class ApiClientManager implements UpClient.Manager {

    private final UpClient.Info info;
    private final Client client;

    ApiClientManager(UpClient.Info info, Client client) {
        this.info = info;
        this.client = client;
    }

    @Override
    public void close() throws IOException {
        try {
            client.close();
        } catch (IllegalStateException cause) {
            throw new IOException("Failed to close the clent: " + cause, cause);
        }
    }

    @Override
    public UpClient.Info getInfo() {
        return info;
    }

}
