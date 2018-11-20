package net.tvburger.up.deploy;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.SpecificationImpl;
import net.tvburger.up.util.Specifications;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class UpApplicationDefinition implements Serializable {

    public static final class Builder {

        private String name;
        private Set<Specification> requiredClasses = new LinkedHashSet<>();
        private Set<Specification> requiredTechnologies = new LinkedHashSet<>();
        private Set<UpEndpointDefinition> endpointDefinitions = new LinkedHashSet<>();
        private Set<UpServiceDefinition> serviceDefinitions = new LinkedHashSet<>();

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withRequiredClass(Class<?> serviceClass) {
            return withRequiredClass(Specifications.forClass(serviceClass));
        }

        public Builder withRequiredClass(Specification serviceClassSpecification) {
            requiredClasses.add(serviceClassSpecification);
            return this;
        }

        public Builder withRequiredTechnology(String name, String version) {
            return withRequiredTechnology(SpecificationImpl.Factory.create(name, version));
        }

        public Builder withRequiredTechnology(Specification endpointTechnology) {
            requiredTechnologies.add(endpointTechnology);
            return this;
        }

        public Builder withEndpointDefinition(UpEndpointDefinition endpointDefinition) {
            endpointDefinitions.add(endpointDefinition);
            requiredClasses.add(endpointDefinition.getInstanceDefinition().getClassSpecification());
            requiredTechnologies.add(endpointDefinition.getEndpointTechnology());
            return this;
        }

        public Builder withServiceDefinition(Class<?> serviceType, Class<?> serviceImplementation, Object... arguments) {
            return withServiceDefinition(UpServiceDefinition.Factory.create(serviceType, serviceImplementation, arguments));
        }

        public Builder withServiceDefinition(UpServiceDefinition serviceDefinition) {
            serviceDefinitions.add(serviceDefinition);
            requiredClasses.add(serviceDefinition.getInstanceDefinition().getClassSpecification());
            return this;
        }

        public UpApplicationDefinition build() {
            return new UpApplicationDefinition(
                    name == null ? "unnamed" : name,
                    Collections.unmodifiableSet(new LinkedHashSet<>(requiredClasses)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(requiredTechnologies)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(endpointDefinitions)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(serviceDefinitions)));
        }

    }

    private final String name;
    private final Set<Specification> requiredClasses;
    private final Set<Specification> requiredTechnologies;
    private final Set<UpEndpointDefinition> endpointDefinitions;
    private final Set<UpServiceDefinition> serviceDefinitions;

    protected UpApplicationDefinition(String name, UpApplicationDefinition deploymentDefinition) {
        this.name = name;
        requiredClasses = deploymentDefinition.requiredClasses;
        requiredTechnologies = deploymentDefinition.requiredTechnologies;
        serviceDefinitions = deploymentDefinition.serviceDefinitions;
        endpointDefinitions = deploymentDefinition.endpointDefinitions;
    }

    private UpApplicationDefinition(String name, Set<Specification> requiredClasses, Set<Specification> requiredTechnologies, Set<UpEndpointDefinition> endpointDefinitions, Set<UpServiceDefinition> serviceDefinitions) {
        this.name = name;
        this.requiredClasses = requiredClasses;
        this.requiredTechnologies = requiredTechnologies;
        this.endpointDefinitions = endpointDefinitions;
        this.serviceDefinitions = serviceDefinitions;
    }

    public String getName() {
        return name;
    }

    public Set<Specification> getRequiredClasses() {
        return requiredClasses;
    }

    public Set<Specification> getRequiredTechnologies() {
        return requiredTechnologies;
    }

    public Set<UpEndpointDefinition> getEndpointDefinitions() {
        return endpointDefinitions;
    }

    public Set<UpServiceDefinition> getServiceDefinitions() {
        return serviceDefinitions;
    }

}
