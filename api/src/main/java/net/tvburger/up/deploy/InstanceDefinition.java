package net.tvburger.up.deploy;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.util.Specifications;

import java.io.Serializable;
import java.util.*;

public class InstanceDefinition implements Serializable {

    public static final class Factory {

        public static InstanceDefinition create(Class<?> instanceClass, Object... arguments) {
            return create(Specifications.forClass(instanceClass), arguments);
        }

        public static InstanceDefinition create(Specification classSpecification, Object... arguments) {
            Objects.requireNonNull(classSpecification);
            List<Object> argumentList = new ArrayList<>();
            if (arguments != null) {
                argumentList.addAll(Arrays.asList(arguments));
            }
            return new InstanceDefinition(
                    classSpecification,
                    Collections.unmodifiableList(new ArrayList<>(argumentList)));
        }

        private Factory() {
        }

    }

    public static final class Builder {

        private Specification classSpecification;
        private List<Object> arguments = new ArrayList<>();

        public Builder withClassSpecification(String instanceClassName) {
            return withClassSpecification(Specifications.forClass(instanceClassName));
        }

        public Builder withClassSpecification(Class<?> instanceClass) {
            return withClassSpecification(Specifications.forClass(instanceClass));
        }

        public Builder withClassSpecification(Specification classSpecification) {
            this.classSpecification = classSpecification;
            return this;
        }

        public Builder withArgument(Object value) {
            arguments.add(value);
            return this;
        }

        public InstanceDefinition build() {
            if (classSpecification == null) {
                throw new IllegalStateException();
            }
            return new InstanceDefinition(classSpecification, Collections.unmodifiableList(new ArrayList<>(arguments)));
        }

    }

    private final Specification classSpecification;
    private final List<Object> arguments;

    protected InstanceDefinition(Specification classSpecification, List<Object> arguments) {
        this.classSpecification = classSpecification;
        this.arguments = arguments;
    }

    public Specification getClassSpecification() {
        return classSpecification;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return String.format("InstanceDefinition{%s, %s}", classSpecification, arguments);
    }

}
