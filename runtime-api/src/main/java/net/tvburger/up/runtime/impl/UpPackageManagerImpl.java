package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpPackage;

public class UpPackageManagerImpl implements UpPackage.Manager {

    private final UpPackage.Info info;

    public UpPackageManagerImpl(UpPackage.Info info) {
        this.info = info;
    }

    @Override
    public UpPackage.Info getInfo() {
        return info;
    }

}
