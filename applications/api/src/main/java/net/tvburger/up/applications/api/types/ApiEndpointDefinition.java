package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.topology.UpEndpointDefinition;

import java.io.IOException;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiEndpointDefinition {

    public static ApiEndpointDefinition fromUp(UpEndpointDefinition up) {
        ApiEndpointDefinition api = new ApiEndpointDefinition();
        api.endpointTechnology = ApiSpecification.fromUp(up.getEndpointTechnology());
        api.instanceDefinition = ApiInstanceDefinition.fromUp(up.getInstanceDefinition());
        api.settings = up.getSettings();
        return api;
    }

    public UpEndpointDefinition toUp() throws IOException {
        UpEndpointDefinition.Builder builder = new UpEndpointDefinition.Builder();
        builder.withEndpointTechnology(endpointTechnology.toUp());
        builder.withInstanceDefinition(instanceDefinition.toUp());
        for (Map.Entry<String, String> setting : settings.entrySet()) {
            builder.withSetting(setting.getKey(), setting.getValue());
        }
        return builder.build();
    }

    private ApiSpecification endpointTechnology;
    private ApiInstanceDefinition instanceDefinition;
    private Map<String, String> settings;

    public Specification getEndpointTechnology() {
        return endpointTechnology;
    }

    public ApiInstanceDefinition getInstanceDefinition() {
        return instanceDefinition;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

}
