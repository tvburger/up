package net.tvburger.up.client.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.security.Identification;

import java.util.Objects;

public class UpClientInfoImpl implements UpClient.Info {

    public static final class Factory {

        public static UpClientInfoImpl create(UpEnvironment.Info environmentInfo, Identification identification) {
            Objects.requireNonNull(environmentInfo);
            Objects.requireNonNull(identification);
            return new UpClientInfoImpl(environmentInfo, identification);
        }

        private Factory() {
        }

    }

    private final UpEnvironment.Info environmentInfo;
    private final Identification identification;

    protected UpClientInfoImpl(UpEnvironment.Info environmentInfo, Identification identification) {
        this.environmentInfo = environmentInfo;
        this.identification = identification;
    }

    @Override
    public UpEnvironment.Info getEnvironmentInfo() {
        return environmentInfo;
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public String toString() {
        return String.format("UpClient.Info{%s, %s}", environmentInfo, identification);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof UpClient.Info
                        && Objects.equals(getEnvironmentInfo(), ((UpClient.Info) object).getEnvironmentInfo())
                        && Objects.equals(getIdentification(), ((UpClient.Info) object).getIdentification()));
    }

    @Override
    public int hashCode() {
        return 19 + Objects.hashCode(environmentInfo) * 13
                + Objects.hashCode(identification) * 37;
    }

}
