package net.tvburger.up.local.impl;

import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.local.impl.LocalUpInstance;

public final class LocalUpClientTarget implements UpClientTarget {

    private final LocalUpInstance instance;

    public LocalUpClientTarget(LocalUpInstance instance) {
        this.instance = instance;
    }

    public LocalUpInstance getInstance() {
        return instance;
    }

}
