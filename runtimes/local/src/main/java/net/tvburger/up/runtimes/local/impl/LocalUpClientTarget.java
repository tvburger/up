package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.client.UpClientTarget;

public final class LocalUpClientTarget implements UpClientTarget {

    private final LocalUpInstance instance;

    public LocalUpClientTarget(LocalUpInstance instance) {
        this.instance = instance;
    }

    public LocalUpInstance getInstance() {
        return instance;
    }

}
