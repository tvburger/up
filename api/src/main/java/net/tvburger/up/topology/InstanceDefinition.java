package net.tvburger.up.topology;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.util.Specifications;

import java.io.Serializable;
import java.util.*;

public class InstanceDefinition implements Serializable {

    public static final class Factory {

        public static InstanceDefinition create(Class<?> instanceClass, Object... arguments) {
            return create(Specifications.forClass(instanceClass));
        }

        public static InstanceDefinition create(Specification instanceSpecification, Object... arguments) {
            Objects.requireNonNull(instanceSpecification);
            List<Object> argumentList = new ArrayList<>();
            if (arguments != null) {
                argumentList.addAll(Arrays.asList(arguments));
            }
            return new InstanceDefinition(
                    instanceSpecification,
                    Collections.unmodifiableList(new ArrayList<>(argumentList)));
        }

        private Factory() {
        }

    }

    public static final class Builder {

        private Specification instanceSpecification;
        private List<Object> arguments = new ArrayList<>();

        public Builder withInstanceSpecification(String instanceClassName) {
            return withInstanceSpecification(Specifications.forClass(instanceClassName));
        }

        public Builder withInstanceSpecification(Class<?> instanceClass) {
            return withInstanceSpecification(Specifications.forClass(instanceClass));
        }

        public Builder withInstanceSpecification(Specification instanceSpecification) {
            this.instanceSpecification = instanceSpecification;
            return this;
        }

        public Builder withArgument(Object value) {
            arguments.add(value);
            return this;
        }

        public InstanceDefinition build() {
            if (instanceSpecification == null) {
                throw new IllegalStateException();
            }
            return new InstanceDefinition(instanceSpecification, Collections.unmodifiableList(new ArrayList<>(arguments)));
        }

    }

    private final Specification instanceSpecification;
    private final List<Object> arguments;

    protected InstanceDefinition(Specification instanceSpecification, List<Object> arguments) {
        this.instanceSpecification = instanceSpecification;
        this.arguments = arguments;
    }

    public Specification getInstanceSpecification() {
        return instanceSpecification;
    }

    public List<Object> getArguments() {
        return arguments;
    }

}
