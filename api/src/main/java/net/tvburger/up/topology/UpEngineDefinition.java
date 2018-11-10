package net.tvburger.up.topology;

import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.impl.ImplementationImpl;
import net.tvburger.up.impl.SpecificationImpl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class UpEngineDefinition {

    public static final class Builder {

        private String hostName;
        private String ip;
        private int port;
        private Implementation engineImplementation;
        private Set<Implementation> endpointImplementations = new LinkedHashSet<>();

        public Builder withHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public Builder withIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withImplementation(String specificationName, String specificationVersion, String implementationName, String implementationVersion) {
            return withImplementation(
                    ImplementationImpl.Factory.create(
                            SpecificationImpl.Factory.create(specificationName, specificationVersion),
                            implementationName,
                            implementationVersion));
        }

        public Builder withImplementation(Implementation engineImplementation) {
            this.engineImplementation = engineImplementation;
            return this;
        }

        public Builder withEndpointImplementation(String specificationName, String specificationVersion, String implementationName, String implementationVersion) {
            return withEndpointImplementation(
                    ImplementationImpl.Factory.create(
                            SpecificationImpl.Factory.create(specificationName, specificationVersion),
                            implementationName,
                            implementationVersion));
        }

        public Builder withEndpointImplementation(Implementation endpointImplementation) {
            endpointImplementations.add(endpointImplementation);
            return this;
        }

        public UpEngineDefinition build() {
            return new UpEngineDefinition(hostName, ip, port, engineImplementation, Collections.unmodifiableSet(new LinkedHashSet<>(endpointImplementations)));
        }

    }

    private final String hostName;
    private final String ip;
    private final int port;
    private final Implementation engineImplementation;
    private final Set<Implementation> endpointImplementations;

    protected UpEngineDefinition(String hostName, String ip, int port, Implementation engineImplementation, Set<Implementation> endpointImplementations) {
        this.hostName = hostName;
        this.ip = ip;
        this.port = port;
        this.engineImplementation = engineImplementation;
        this.endpointImplementations = endpointImplementations;
    }

    public String getHostName() {
        return hostName;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Implementation getEngineImplementation() {
        return engineImplementation;
    }

    public Set<Implementation> getEndpointImplementations() {
        return endpointImplementations;
    }

}
