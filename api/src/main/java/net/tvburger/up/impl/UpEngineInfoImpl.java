package net.tvburger.up.impl;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.UpEngineInfo;
import net.tvburger.up.security.Identification;

import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;

public class UpEngineInfoImpl implements UpEngineInfo {

    public static final class Factory {

        public static UpEngineInfoImpl create(UUID uuid, InetAddress host, int port, Identification identification, Specification specification) {
            Objects.requireNonNull(uuid);
            Objects.requireNonNull(host);
            if (port < -1 || port == 0) {
                throw new IllegalArgumentException("Invalid port number: -1 or positive!");
            }
            Objects.requireNonNull(identification);
            Objects.requireNonNull(specification);
            return new UpEngineInfoImpl(uuid, host, port, identification, specification);
        }

    }

    private final UUID uuid;
    private final InetAddress host;
    private final int port;
    private final Identification identification;
    private final Specification specification;

    protected UpEngineInfoImpl(UUID uuid, InetAddress host, int port, Identification identification, Specification specification) {
        this.uuid = uuid;
        this.host = host;
        this.port = port;
        this.identification = identification;
        this.specification = specification;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public InetAddress getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public String getSpecificationName() {
        return specification.getSpecificationName();
    }

    @Override
    public String getSpecificationVersion() {
        return specification.getSpecificationVersion();
    }

    @Override
    public String toString() {
        return String.format("UpEngineInfo{%s, %s, %s, %s}", host, port, identification, specification);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof UpEngineInfo
                        && Objects.equals(getUuid(), ((UpEngineInfo) object).getUuid())
                        && Objects.equals(getHost(), ((UpEngineInfo) object).getHost())
                        && Objects.equals(getPort(), ((UpEngineInfo) object).getPort())
                        && Objects.equals(getIdentification(), ((UpEngineInfo) object).getIdentification())
                        && Objects.equals(getSpecificationName(), ((UpEngineInfo) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((UpEngineInfo) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return 9 + Objects.hashCode(uuid) * 13
                + Objects.hashCode(host) * 47
                + Objects.hashCode(port) * 31
                + Objects.hashCode(identification) * 11
                + Objects.hashCode(specification) * 17;
    }

}
