package net.tvburger.up.impl;

import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.client.UpClientInfo;
import net.tvburger.up.security.Identification;

import java.util.Objects;

public class UpClientInfoImpl implements UpClientInfo {

    public static final class Factory {

        public static UpClientInfoImpl create(EnvironmentInfo environmentInfo, Identification identification) {
            Objects.requireNonNull(environmentInfo);
            Objects.requireNonNull(identification);
            return new UpClientInfoImpl(environmentInfo, identification);
        }

        private Factory() {
        }

    }

    private final EnvironmentInfo environmentInfo;
    private final Identification identification;

    protected UpClientInfoImpl(EnvironmentInfo environmentInfo, Identification identification) {
        this.environmentInfo = environmentInfo;
        this.identification = identification;
    }

    @Override
    public EnvironmentInfo getEnvironmentInfo() {
        return environmentInfo;
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public String toString() {
        return String.format("UpClientInfo{%s, %s}", environmentInfo, identification);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof UpClientInfo
                        && Objects.equals(getEnvironmentInfo(), ((UpClientInfo) object).getEnvironmentInfo())
                        && Objects.equals(getIdentification(), ((UpClientInfo) object).getIdentification()));
    }

    @Override
    public int hashCode() {
        return 19 + Objects.hashCode(environmentInfo) * 13
                + Objects.hashCode(identification) * 37;
    }

}
