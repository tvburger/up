package net.tvburger.up.topology;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class UpRuntimeTopology implements Serializable {

    public static final class Factory {

        public static UpRuntimeTopology create(UpEngineDefinition... engineDefinitions) {
            if (engineDefinitions == null || engineDefinitions.length == 0) {
                throw new IllegalArgumentException();
            }
            Builder builder = new Builder();
            for (UpEngineDefinition definition : engineDefinitions) {
                builder.withEngineDefinition(definition);
            }
            return builder.build();
        }

        private Factory() {
        }

    }

    public static final class Builder {

        private Set<UpEngineDefinition> engineDefinitions = new LinkedHashSet<>();

        public Builder withEngineDefinition(UpEngineDefinition engineDefinition) {
            engineDefinitions.add(engineDefinition);
            return this;
        }

        public UpRuntimeTopology build() {
            if (engineDefinitions.isEmpty()) {
                throw new IllegalStateException();
            }
            return new UpRuntimeTopology(Collections.unmodifiableSet(new LinkedHashSet<>(engineDefinitions)));
        }

    }

    private final Set<UpEngineDefinition> engineDefinitions;

    protected UpRuntimeTopology(UpRuntimeTopology runtimeDefinition) {
        this.engineDefinitions = runtimeDefinition.engineDefinitions;
    }

    private UpRuntimeTopology(Set<UpEngineDefinition> engineDefinitions) {
        this.engineDefinitions = engineDefinitions;
    }

    public Set<UpEngineDefinition> getEngineDefinitions() {
        return engineDefinitions;
    }

}
