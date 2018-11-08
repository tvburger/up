package net.tvburger.up.local;

import net.tvburger.up.client.UpClientTarget;

public final class LocalUpClientTarget implements UpClientTarget {

    private final LocalUpInstance instance;

    LocalUpClientTarget(LocalUpInstance instance) {
        this.instance = instance;
    }

    public LocalUpInstance getInstance() {
        return instance;
    }

}
