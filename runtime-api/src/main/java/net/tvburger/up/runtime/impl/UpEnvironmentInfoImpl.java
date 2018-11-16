package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpRuntimeInfo;
import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;

import java.util.Objects;

public class UpEnvironmentInfoImpl implements UpEnvironment.Info {

    public static final class Factory {

        public static UpEnvironmentInfoImpl create(String name, UpRuntimeInfo runtimeInfo, Identity identity) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(runtimeInfo);
            Objects.requireNonNull(identity);
            return new UpEnvironmentInfoImpl(name, runtimeInfo, identity);
        }

        private Factory() {
        }

    }

    private final String name;
    private final UpRuntimeInfo runtimeInfo;
    private final Identification identification;

    protected UpEnvironmentInfoImpl(String name, UpRuntimeInfo runtimeInfo, Identification identification) {
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
        return String.format("UpEnvironment.Info{%s, %s, %s}", name, runtimeInfo, identification);
    }

    @Override
    public boolean equals(Object object) {
        return this == object
                || null != object &&
                (object instanceof UpEnvironment.Info
                        && Objects.equals(getName(), ((UpEnvironment.Info) object).getName())
                        && Objects.equals(getRuntimeInfo(), ((UpEnvironment.Info) object).getRuntimeInfo())
                        && Objects.equals(getIdentification(), ((UpEnvironment.Info) object).getIdentification()));
    }

    @Override
    public int hashCode() {
        return 13 + Objects.hashCode(name) * 31
                + Objects.hashCode(runtimeInfo) * 91
                + Objects.hashCode(identification) * 47;
    }

}
