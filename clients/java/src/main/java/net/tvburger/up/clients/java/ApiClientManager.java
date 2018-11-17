package net.tvburger.up.clients.java;

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
        client.close();
    }

    @Override
    public UpClient.Info getInfo() {
        return info;
    }

}
