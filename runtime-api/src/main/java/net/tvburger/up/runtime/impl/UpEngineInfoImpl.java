package net.tvburger.up.runtime.impl;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.security.Identification;

import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;

public class UpEngineInfoImpl implements UpEngine.Info {

    public static final class Factory {

        public static UpEngineInfoImpl create(InetAddress host, int port, Identification identification, Specification specification) {
            Objects.requireNonNull(host);
            if (port < -1 || port == 0) {
                throw new IllegalArgumentException("Invalid port number: -1 or positive!");
            }
            Objects.requireNonNull(identification);
            Objects.requireNonNull(specification);
            return new UpEngineInfoImpl(host, port, identification, specification);
        }

    }

    private final InetAddress host;
    private final int port;
    private final Identification identification;
    private final Specification specification;

    protected UpEngineInfoImpl(InetAddress host, int port, Identification identification, Specification specification) {
        this.host = host;
        this.port = port;
        this.identification = identification;
        this.specification = specification;
    }

    @Override
    public UUID getUuid() {
        return identification.getUuid();
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
        return String.format("UpEngine.Info{%s, %s, %s, %s}", host, port, identification, specification);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof UpEngine.Info
                        && Objects.equals(getUuid(), ((UpEngine.Info) object).getUuid())
                        && Objects.equals(getHost(), ((UpEngine.Info) object).getHost())
                        && Objects.equals(getPort(), ((UpEngine.Info) object).getPort())
                        && Objects.equals(getIdentification(), ((UpEngine.Info) object).getIdentification())
                        && Objects.equals(getSpecificationName(), ((UpEngine.Info) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((UpEngine.Info) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, identification, specification);
    }

}
