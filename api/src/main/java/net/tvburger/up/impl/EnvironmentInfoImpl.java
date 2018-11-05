package net.tvburger.up.impl;

import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.identity.Identity;

public class EnvironmentInfoImpl implements EnvironmentInfo {

    private final String name;
    private final Identity identity;

    public EnvironmentInfoImpl(String name, Identity identity) {
        this.name = name;
        this.identity = identity;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Identity getEnvironmentIdentity() {
        return identity;
    }

}
