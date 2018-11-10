package net.tvburger.up.impl;

import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.runtime.UpRuntimeInfo;
import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;

import java.util.Objects;

public class EnvironmentInfoImpl implements EnvironmentInfo {

    public static final class Factory {

        public static EnvironmentInfoImpl create(String name, UpRuntimeInfo runtimeInfo, Identity identity) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(runtimeInfo);
            Objects.requireNonNull(identity);
            return new EnvironmentInfoImpl(name, runtimeInfo, identity);
        }

        private Factory() {
        }

    }

    private final String name;
    private final UpRuntimeInfo runtimeInfo;
    private final Identification identification;

    protected EnvironmentInfoImpl(String name, UpRuntimeInfo runtimeInfo, Identification identification) {
        this.name = name;
        this.runtimeInfo = runtimeInfo;
        this.identification = identification;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UpRuntimeInfo getRuntimeInfo() {
        return runtimeInfo;
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public String toString() {
        return String.format("EnvironmentInfo{%s, %s, %s}", name, runtimeInfo, identification);
    }

    @Override
    public boolean equals(Object object) {
        return this == object
                || null != object &&
                (object instanceof EnvironmentInfo
                        && Objects.equals(getName(), ((EnvironmentInfo) object).getName())
                        && Objects.equals(getRuntimeInfo(), ((EnvironmentInfo) object).getRuntimeInfo())
                        && Objects.equals(getIdentification(), ((EnvironmentInfo) object).getIdentification()));
    }

    @Override
    public int hashCode() {
        return 13 + Objects.hashCode(name) * 31
                + Objects.hashCode(runtimeInfo) * 91
                + Objects.hashCode(identification) * 47;
    }

}
