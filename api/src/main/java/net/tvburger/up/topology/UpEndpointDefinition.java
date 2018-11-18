package net.tvburger.up.topology;

import net.tvburger.up.behaviors.Specification;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class UpEndpointDefinition implements Serializable {

    public static final class Builder {

        private Specification endpointTechnology;
        private InstanceDefinition instanceDefinition;
        private Map<String, String> settings = new LinkedHashMap<>();

        public Builder withEndpointTechnology(Specification endpointTechnology) {
            this.endpointTechnology = endpointTechnology;
            return this;
        }

        public Builder withInstanceDefinition(Class<?> endpointClass, Object... arguments) {
            InstanceDefinition.Builder builder = new InstanceDefinition.Builder()
                    .withClassSpecification(endpointClass);
            if (arguments != null) {
                for (Object argument : arguments) {
                    builder.withArgument(argument);
                }
            }
            return withInstanceDefinition(builder.build());
        }

        public Builder withInstanceDefinition(InstanceDefinition instanceDefinition) {
            this.instanceDefinition = instanceDefinition;
            return this;
        }

        public Builder withSetting(String name, String value) {
            settings.put(name, value);
            return this;
        }

        public UpEndpointDefinition build() {
            if (instanceDefinition == null || endpointTechnology == null) {
                throw new IllegalStateException();
            }
            return new UpEndpointDefinition(endpointTechnology, instanceDefinition, Collections.unmodifiableMap(new LinkedHashMap<>(settings)));
        }

    }

    private final Specification endpointTechnology;
    private final InstanceDefinition instanceDefinition;
    private final Map<String, String> settings;

    protected UpEndpointDefinition(Specification endpointTechnology, InstanceDefinition instanceDefinition, Map<String, String> settings) {
        this.endpointTechnology = endpointTechnology;
        this.instanceDefinition = instanceDefinition;
        this.settings = settings;
    }

    public Specification getEndpointTechnology() {
        return endpointTechnology;
    }

    public InstanceDefinition getInstanceDefinition() {
        return instanceDefinition;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

}
