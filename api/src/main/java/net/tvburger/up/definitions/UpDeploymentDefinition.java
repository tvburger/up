package net.tvburger.up.definitions;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.impl.SpecificationImpl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class UpDeploymentDefinition {

    public static final class Builder {

        private Set<Class<?>> serviceImplementations = new LinkedHashSet<>();
        private Set<Specification> endpointTechnologies = new LinkedHashSet<>();
        private Set<EndpointDefinition> endpointDefinitions = new LinkedHashSet<>();
        private Set<ServiceDefinition> serviceDefinitions = new LinkedHashSet<>();

        public Builder withServiceImplementations(Class<?> serviceClass) {
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

        public Builder withEndpointDefinition(String name, String version, ServiceDefinition serviceDefinition, Object... arguments) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(version);
            return withEndpointDefinition(SpecificationImpl.Factory.create(name, version), serviceDefinition, arguments);
        }

        public Builder withEndpointDefinition(Specification specification, ServiceDefinition serviceDefinition, Object... arguments) {
            Objects.requireNonNull(specification);
            Objects.requireNonNull(serviceDefinition);
            EndpointDefinition.Builder builder = new EndpointDefinition.Builder()
                    .withEndpointTechnology(specification)
                    .withServiceDefinition(serviceDefinition);
            if (arguments != null) {
                for (Object argument : arguments) {
                    builder.withArgument(argument);
                }
            }
            return withEndpointDefinition(builder.build());
        }

        public Builder withEndpointDefinition(EndpointDefinition endpointDefinition) {
            endpointDefinitions.add(endpointDefinition);
            serviceImplementations.add(endpointDefinition.getServiceDefinition().getServiceImplementation());
            endpointTechnologies.add(endpointDefinition.getEndpointTechnology());
            return this;
        }

        public Builder withServiceDefinition(Class<?> serviceType, Class<?> serviceImplementation, Object... arguments) {
            return withServiceDefinition(ServiceDefinition.Factory.create(serviceType, serviceImplementation, arguments));
        }

        public Builder withServiceDefinition(ServiceDefinition serviceDefinition) {
            serviceDefinitions.add(serviceDefinition);
            return this;
        }

        public UpDeploymentDefinition build() {
            return new UpDeploymentDefinition(
                    Collections.unmodifiableSet(new LinkedHashSet<>(serviceImplementations)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(endpointTechnologies)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(endpointDefinitions)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(serviceDefinitions)));
        }

    }

    private final Set<Class<?>> serviceImplementations;
    private final Set<Specification> endpointTechnologies;
    private final Set<EndpointDefinition> endpointDefinitions;
    private final Set<ServiceDefinition> serviceDefinitions;

    protected UpDeploymentDefinition(UpDeploymentDefinition deploymentDefinition) {
        serviceImplementations = deploymentDefinition.serviceImplementations;
        endpointTechnologies = deploymentDefinition.endpointTechnologies;
        serviceDefinitions = deploymentDefinition.serviceDefinitions;
        endpointDefinitions = deploymentDefinition.endpointDefinitions;
    }

    private UpDeploymentDefinition(Set<Class<?>> serviceImplementations, Set<Specification> endpointTechnologies, Set<EndpointDefinition> endpointDefinitions, Set<ServiceDefinition> serviceDefinitions) {
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

    public Set<EndpointDefinition> getEndpointDefinitions() {
        return endpointDefinitions;
    }

    public Set<ServiceDefinition> getServiceDefinitions() {
        return serviceDefinitions;
    }

}
