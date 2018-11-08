package net.tvburger.up.definitions;

import java.util.*;

public class ServiceDefinition {

    public static final class Factory {

        public static ServiceDefinition create(Class<?> serviceType, Class<?> serviceImplementation, Object... arguments) {
            Objects.requireNonNull(serviceType);
            List<Object> argumentList = new ArrayList<>();
            if (arguments != null) {
                argumentList.addAll(Arrays.asList(arguments));
            }
            return new ServiceDefinition(serviceType, serviceImplementation, Collections.unmodifiableList(new ArrayList<>(argumentList)));
        }

        private Factory() {
        }

    }

    public static final class Builder {

        private Class<?> serviceType;
        private Class<?> serviceImplementation;
        private List<Object> arguments = new ArrayList<>();

        public Builder withServiceType(Class<?> serviceType) {
            this.serviceType = serviceType;
            return this;
        }

        public Builder withServiceImplementation(Class<?> serviceImplementation) {
            this.serviceImplementation = serviceImplementation;
            return this;
        }

        public Builder withArgument(Object value) {
            arguments.add(value);
            return this;
        }

        public ServiceDefinition build() {
            if (serviceType == null || serviceImplementation == null || !serviceType.isAssignableFrom(serviceImplementation)) {
                throw new IllegalStateException();
            }
            return new ServiceDefinition(serviceType, serviceImplementation, Collections.unmodifiableList(new ArrayList<>(arguments)));
        }

    }

    private final Class<?> serviceType;
    private final Class<?> serviceImplementation;
    private final List<Object> arguments;

    protected ServiceDefinition(Class<?> serviceType, Class<?> serviceImplementation, List<Object> arguments) {
        this.serviceType = serviceType;
        this.serviceImplementation = serviceImplementation;
        this.arguments = arguments;
    }

    public Class<?> getServiceType() {
        return serviceType;
    }

    public Class<?> getServiceImplementation() {
        return serviceImplementation;
    }

    public List<Object> getArguments() {
        return arguments;
    }

}
