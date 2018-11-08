package net.tvburger.up.definitions;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.impl.SpecificationImpl;

import java.util.*;

public class UpDeploymentDefinition {

    public static final class Builder {

        private Map<Class<?>, Class<?>> serviceTypes = new LinkedHashMap<>();
        private Set<Specification> endpointReferences = new LinkedHashSet<>();
        private Set<EndpointDefinition> endpointDefinitions = new LinkedHashSet<>();
        private Set<ServiceDefinition> serviceDefinitions = new LinkedHashSet<>();

        public Builder withServiceType(Class<?> serviceType, Class<?> serviceClass) {
            serviceTypes.put(serviceType, serviceClass);
            return this;
        }

        public Builder withEndpointReference(String name, String version) {
            return withEndpointReference(SpecificationImpl.Factory.create(name, version));
        }

        public Builder withEndpointReference(Specification endpointReference) {
            endpointReferences.add(endpointReference);
            return this;
        }

        public Builder withEndpointDefinition(String name, String version, ServiceDefinition serviceDefinition, Object... arguments) {
            EndpointDefinition.Builder builder = new EndpointDefinition.Builder()
                    .withEndpointReference(SpecificationImpl.Factory.create(name, version))
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
            endpointReferences.add(endpointDefinition.getEndpointReference());
            return this;
        }

        public Builder withServiceDefinition(Class<?> serviceType, Object... arguments) {
            return withServiceDefinition(ServiceDefinition.Factory.create(serviceType, arguments));
        }

        public Builder withServiceDefinition(ServiceDefinition serviceDefinition) {
            serviceDefinitions.add(serviceDefinition);
            return this;
        }

        public UpDeploymentDefinition build() {
            return new UpDeploymentDefinition(
                    Collections.unmodifiableMap(new LinkedHashMap<>(serviceTypes)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(endpointReferences)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(endpointDefinitions)),
                    Collections.unmodifiableSet(new LinkedHashSet<>(serviceDefinitions)));
        }

    }

    private final Map<Class<?>, Class<?>> serviceTypes;
    private final Set<Specification> endpointReferences;
    private final Set<EndpointDefinition> endpointDefinitions;
    private final Set<ServiceDefinition> serviceDefinitions;

    protected UpDeploymentDefinition(UpDeploymentDefinition deploymentDefinition) {
        serviceTypes = deploymentDefinition.serviceTypes;
        endpointReferences = deploymentDefinition.endpointReferences;
        serviceDefinitions = deploymentDefinition.serviceDefinitions;
        endpointDefinitions = deploymentDefinition.endpointDefinitions;
    }

    private UpDeploymentDefinition(Map<Class<?>, Class<?>> serviceTypes, Set<Specification> endpointReferences, Set<EndpointDefinition> endpointDefinitions, Set<ServiceDefinition> serviceDefinitions) {
        this.serviceTypes = serviceTypes;
        this.endpointReferences = endpointReferences;
        this.endpointDefinitions = endpointDefinitions;
        this.serviceDefinitions = serviceDefinitions;
    }

    public Map<Class<?>, Class<?>> getServiceTypes() {
        return serviceTypes;
    }

    public Set<Specification> getEndpointReferences() {
        return endpointReferences;
    }

    public Set<EndpointDefinition> getEndpointDefinitions() {
        return endpointDefinitions;
    }

    public Set<ServiceDefinition> getServiceDefinitions() {
        return serviceDefinitions;
    }

}
