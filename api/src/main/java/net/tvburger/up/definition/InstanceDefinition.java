package net.tvburger.up.definition;

import java.util.*;

public class InstanceDefinition {

    public static final class Factory {

        public static InstanceDefinition create(Class<?> instanceClass, Object... arguments) {
            Objects.requireNonNull(instanceClass);
            List<Object> argumentList = new ArrayList<>();
            if (arguments != null) {
                argumentList.addAll(Arrays.asList(arguments));
            }
            return new InstanceDefinition(instanceClass, Collections.unmodifiableList(new ArrayList<>(argumentList)));
        }

        private Factory() {
        }

    }

    public static final class Builder {

        private Class<?> instanceClass;
        private List<Object> arguments = new ArrayList<>();

        public Builder withInstanceClass(Class<?> instanceClass) {
            this.instanceClass = instanceClass;
            return this;
        }

        public Builder withArgument(Object value) {
            arguments.add(value);
            return this;
        }

        public InstanceDefinition build() {
            if (instanceClass == null) {
                throw new IllegalStateException();
            }
            return new InstanceDefinition(instanceClass, Collections.unmodifiableList(new ArrayList<>(arguments)));
        }

    }

    private final Class<?> instanceClass;
    private final List<Object> arguments;

    protected InstanceDefinition(Class<?> instanceClass, List<Object> arguments) {
        this.instanceClass = instanceClass;
        this.arguments = arguments;
    }

    public Class<?> getInstanceClass() {
        return instanceClass;
    }

    public List<Object> getArguments() {
        return arguments;
    }

}
