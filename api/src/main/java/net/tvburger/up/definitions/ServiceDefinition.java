package net.tvburger.up.definitions;

import java.util.*;

public class ServiceDefinition {

    public static final class Factory {

        public static ServiceDefinition create(Class<?> serviceType, Object... arguments) {
            Objects.requireNonNull(serviceType);
            List<Object> argumentList = new ArrayList<>();
            if (arguments != null) {
                argumentList.addAll(Arrays.asList(arguments));
            }
            return new ServiceDefinition(serviceType, Collections.unmodifiableList(new ArrayList<>(argumentList)));
        }

        private Factory() {
        }

    }

    public static final class Builder {

        private List<Object> arguments = new ArrayList<>();
        private Class<?> serviceType;

        public Builder withArgument(Object value) {
            arguments.add(value);
            return this;
        }

        public Builder withServiceType(Class<?> serviceType) {
            this.serviceType = serviceType;
            return this;
        }

        public ServiceDefinition build() {
            if (serviceType == null) {
                throw new IllegalStateException();
            }
            return new ServiceDefinition(serviceType, Collections.unmodifiableList(new ArrayList<>(arguments)));
        }

    }

    private final Class<?> serviceType;
    private final List<Object> arguments;

    protected ServiceDefinition(Class<?> serviceType, List<Object> arguments) {
        this.serviceType = serviceType;
        this.arguments = arguments;
    }

    public Class<?> getServiceType() {
        return serviceType;
    }

    public List<Object> getArguments() {
        return arguments;
    }

}
