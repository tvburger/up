package net.tvburger.up.topology;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.SpecificationImpl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class UpApplicationTopology implements Serializable {

    public static final class Builder {

        private Set<Class<?>> serviceImplementations = new LinkedHashSet<>();
        private Set<Specification> endpointTechnologies = new LinkedHashSet<>();
        private Set<UpEndpointDefinition> endpointDefinitions = new LinkedHashSet<>();
        private Set<UpServiceDefinition> serviceDefinitions = new LinkedHashSet<>();

        public Builder withServiceImplementation(Class<?> serviceClass) {
            serviceImplementations.add(serviceClass);
            return this;
        }

        public Builder withEndpointTechnology(String name, String version) {
            return withEndpointTechnology(SpecificationImpl.Factory.create(name, version));
        }

        public Builder withEndpointTechnology(Specification endpointTechnology) {
            endpointTechnologies.add(endpointTechnology);
            return this;
        }

        public Builder withEndpointDefinition(UpEndpointDefinition endpointDefinition) {
            endpointDefinitions.add(endpointDefinition);
            endpointTechnologies.add(endpointDefinition.getEndpointTechnology());
            return this;
        }

        public Builder withServiceDefinition(Class<?> serviceType, Class<?> serviceImplementation, Object... arguments) {
            return withServiceDefinition(UpServiceDefinition.Factory.create(serviceType, serviceImplementation, arguments));
        }

        public Builder withServiceDefinition(UpServiceDefinition serviceDefinition) {
            serviceDefinitions.add(serviceDefinition);
            serviceImplementations.add(serviceDefinition.getInstanceDefinition().getInstanceClass());
            return this;
        }

        public UpApplicationTopology build() {
            return new UpApplicationTopology(
                    Collections.unmodifiableSet(new LinkedHashSet<>(serviceImplementations)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(endpointTechnologies)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(endpointDefinitions)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(serviceDefinitions)));
        }

    }

    private final Set<Class<?>> serviceImplementations;
    private final Set<Specification> endpointTechnologies;
    private final Set<UpEndpointDefinition> endpointDefinitions;
    private final Set<UpServiceDefinition> serviceDefinitions;

    protected UpApplicationTopology(UpApplicationTopology deploymentDefinition) {
        serviceImplementations = deploymentDefinition.serviceImplementations;
        endpointTechnologies = deploymentDefinition.endpointTechnologies;
        serviceDefinitions = deploymentDefinition.serviceDefinitions;
        endpointDefinitions = deploymentDefinition.endpointDefinitions;
    }

    private UpApplicationTopology(Set<Class<?>> serviceImplementations, Set<Specification> endpointTechnologies, Set<UpEndpointDefinition> endpointDefinitions, Set<UpServiceDefinition> serviceDefinitions) {
        this.serviceImplementations = serviceImplementations;
        this.endpointTechnologies = endpointTechnologies;
        this.endpointDefinitions = endpointDefinitions;
        this.serviceDefinitions = serviceDefinitions;
    }

    public Set<Class<?>> getServiceImplementations() {
        return serviceImplementations;
    }

    public Set<Specification> getEndpointTechnologies() {
        return endpointTechnologies;
    }

    public Set<UpEndpointDefinition> getEndpointDefinitions() {
        return endpointDefinitions;
    }

    public Set<UpServiceDefinition> getServiceDefinitions() {
        return serviceDefinitions;
    }

}
