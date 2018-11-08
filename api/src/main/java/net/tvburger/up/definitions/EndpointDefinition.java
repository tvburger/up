package net.tvburger.up.definitions;

import net.tvburger.up.behaviors.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndpointDefinition {

    public static final class Builder {

        private Specification endpointTechnology;
        private ServiceDefinition serviceDefinition;
        private List<Object> arguments = new ArrayList<>();

        public Builder withEndpointTechnology(Specification endpointTechnology) {
            this.endpointTechnology = endpointTechnology;
            return this;
        }

        public Builder withServiceDefinition(Class<?> serviceType, Class<?> serviceImplementation, Object... arguments) {
            return withServiceDefinition(ServiceDefinition.Factory.create(serviceType, serviceImplementation, arguments));
        }

        public Builder withServiceDefinition(ServiceDefinition serviceDefinition) {
            this.serviceDefinition = serviceDefinition;
            return this;
        }

        public Builder withArgument(Object value) {
            arguments.add(value);
            return this;
        }

        public EndpointDefinition build() {
            if (serviceDefinition == null || endpointTechnology == null) {
                throw new IllegalStateException();
            }
            return new EndpointDefinition(endpointTechnology, serviceDefinition, Collections.unmodifiableList(new ArrayList<>(arguments)));
        }

    }

    private final Specification endpointTechnology;
    private final ServiceDefinition serviceDefinition;
    private final List<Object> arguments;

    protected EndpointDefinition(Specification endpointTechnology, ServiceDefinition serviceDefinition, List<Object> arguments) {
        this.endpointTechnology = endpointTechnology;
        this.serviceDefinition = serviceDefinition;
        this.arguments = arguments;
    }

    public Specification getEndpointTechnology() {
        return endpointTechnology;
    }

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public List<Object> getArguments() {
        return arguments;
    }

}
