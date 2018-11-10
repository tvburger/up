package net.tvburger.up.definition;

import net.tvburger.up.behaviors.Specification;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class EndpointDefinition {

    public static final class Builder {

        private Specification endpointTechnology;
        private InstanceDefinition instanceDefinition;
        private Map<String, String> settings = new LinkedHashMap<>();

        public Builder withEndpointTechnology(Specification endpointTechnology) {
            this.endpointTechnology = endpointTechnology;
            return this;
        }

        public Builder withEndpointDefinition(Class<?> endpointClass, Object... arguments) {
            InstanceDefinition.Builder builder = new InstanceDefinition.Builder()
                    .withInstanceClass(endpointClass);
            if (arguments != null) {
                for (Object argument : arguments) {
                    builder.withArgument(argument);
                }
            }
            return withEndpointDefinition(builder.build());
        }

        public Builder withEndpointDefinition(InstanceDefinition instanceDefinition) {
            this.instanceDefinition = instanceDefinition;
            return this;
        }

        public Builder withSetting(String name, String value) {
            settings.put(name, value);
            return this;
        }

        public EndpointDefinition build() {
            if (instanceDefinition == null || endpointTechnology == null) {
                throw new IllegalStateException();
            }
            return new EndpointDefinition(endpointTechnology, instanceDefinition, Collections.unmodifiableMap(new LinkedHashMap<>(settings)));
        }

    }

    private final Specification endpointTechnology;
    private final InstanceDefinition instanceDefinition;
    private final Map<String, String> settings;

    protected EndpointDefinition(Specification endpointTechnology, InstanceDefinition instanceDefinition, Map<String, String> settings) {
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
