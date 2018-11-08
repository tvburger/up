package net.tvburger.up.definitions;

import net.tvburger.up.behaviors.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndpointDefinition {

    public static final class Builder {

        private Specification endpointReference;
        private ServiceDefinition serviceDefinition;
        private List<Object> arguments = new ArrayList<>();

        public Builder withEndpointReference(Specification endpointReference) {
            this.endpointReference = endpointReference;
            return this;
        }

        public Builder withServiceDefinition(Class<?> serviceType, Object... arguments) {
            return withServiceDefinition(ServiceDefinition.Factory.create(serviceType, arguments));
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
            if (serviceDefinition == null || endpointReference == null) {
                throw new IllegalStateException();
            }
            return new EndpointDefinition(endpointReference, serviceDefinition, Collections.unmodifiableList(new ArrayList<>(arguments)));
        }

    }

    private final Specification endpointReference;
    private final ServiceDefinition serviceDefinition;
    private final List<Object> arguments;

    protected EndpointDefinition(Specification endpointReference, ServiceDefinition serviceDefinition, List<Object> arguments) {
        this.endpointReference = endpointReference;
        this.serviceDefinition = serviceDefinition;
        this.arguments = arguments;
    }

    public Specification getEndpointReference() {
        return endpointReference;
    }

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public List<Object> getArguments() {
        return arguments;
    }

}
