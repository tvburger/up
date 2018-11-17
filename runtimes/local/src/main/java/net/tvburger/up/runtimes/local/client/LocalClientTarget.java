package net.tvburger.up.runtimes.local.client;

import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.runtimes.local.LocalInstance;

public final class LocalClientTarget implements UpClientTarget {

    private final LocalInstance instance;

    public LocalClientTarget(LocalInstance instance) {
        this.instance = instance;
    }

    public LocalInstance getInstance() {
        return instance;
    }

}
