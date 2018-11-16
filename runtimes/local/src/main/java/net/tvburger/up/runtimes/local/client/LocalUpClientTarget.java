package net.tvburger.up.runtimes.local.client;

import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.runtimes.local.LocalUpInstance;

public final class LocalUpClientTarget implements UpClientTarget {

    private final LocalUpInstance instance;

    public LocalUpClientTarget(LocalUpInstance instance) {
        this.instance = instance;
    }

    public LocalUpInstance getInstance() {
        return instance;
    }

}
