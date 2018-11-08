package net.tvburger.up.local.impl;

import net.tvburger.up.client.UpClientInfo;
import net.tvburger.up.client.UpClientManager;

import java.io.IOException;

public final class LocalUpClientManager implements UpClientManager {

    private final UpClientInfo info;

    public LocalUpClientManager(UpClientInfo info) {
        this.info = info;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public UpClientInfo getInfo() {
        return info;
    }

}
