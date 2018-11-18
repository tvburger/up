package net.tvburger.up.topology;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.util.Specifications;

import java.io.Serializable;
import java.util.*;

public class UpServiceDefinition implements Serializable {

    public static final class Factory {

        public static UpServiceDefinition create(Specification serviceType, InstanceDefinition instanceDefinition) {
            Objects.requireNonNull(serviceType);
            Objects.requireNonNull(instanceDefinition);
            return new UpServiceDefinition(serviceType, instanceDefinition);
        }

        public static UpServiceDefinition create(Class<?> serviceType, InstanceDefinition instanceDefinition) {
            Objects.requireNonNull(serviceType);
            Objects.requireNonNull(instanceDefinition);
            return new UpServiceDefinition(Specifications.forClass(serviceType), instanceDefinition);
        }

        public static UpServiceDefinition create(Class<?> serviceType, Class<?> serviceImplementation, Object... arguments) {
            Objects.requireNonNull(serviceType);
            List<Object> argumentList = new ArrayList<>();
            if (arguments != null) {
                argumentList.addAll(Arrays.asList(arguments));
            }
            return new UpServiceDefinition(
                    Specifications.forClass(serviceType),
                    new InstanceDefinition(
                            Specifications.forClass(serviceImplementation),
                            Collections.unmodifiableList(new ArrayList<>(argumentList))));
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

        public UpServiceDefinition build() {
            if (serviceType == null || serviceImplementation == null || !serviceType.isAssignableFrom(serviceImplementation)) {
                throw new IllegalStateException();
            }
            return new UpServiceDefinition(
                    Specifications.forClass(serviceType),
                    new InstanceDefinition(
                            Specifications.forClass(serviceImplementation),
                            Collections.unmodifiableList(new ArrayList<>(arguments))));
        }

    }

    private final Specification serviceType;
    private final InstanceDefinition instanceDefinition;

    protected UpServiceDefinition(Specification serviceType, InstanceDefinition instanceDefinition) {
        this.serviceType = serviceType;
        this.instanceDefinition = instanceDefinition;
    }

    public Specification getServiceType() {
        return serviceType;
    }

    public InstanceDefinition getInstanceDefinition() {
        return instanceDefinition;
    }

}
