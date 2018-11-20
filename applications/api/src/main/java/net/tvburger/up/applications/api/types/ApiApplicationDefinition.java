package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.deploy.UpEndpointDefinition;
import net.tvburger.up.deploy.UpServiceDefinition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiApplicationDefinition {

    public static ApiApplicationDefinition fromUp(UpApplicationDefinition up) {
        ApiApplicationDefinition api = new ApiApplicationDefinition();
        for (Specification specification : up.getRequiredClasses()) {
            api.requiredClasses.add(ApiSpecification.fromUp(specification));
        }
        for (Specification specification : up.getRequiredTechnologies()) {
            api.requiredTechnologies.add(ApiSpecification.fromUp(specification));
        }
        for (UpEndpointDefinition definition : up.getEndpointDefinitions()) {
            api.endpointDefinitions.add(ApiEndpointDefinition.fromUp(definition));
        }
        for (UpServiceDefinition definition : up.getServiceDefinitions()) {
            api.serviceDefinitions.add(ApiServiceDefinition.fromUp(definition));
        }
        return api;
    }

    public UpApplicationDefinition toUp() throws IOException {
        UpApplicationDefinition.Builder builder = new UpApplicationDefinition.Builder();
        for (ApiSpecification serviceImplementation : requiredClasses) {
            builder.withRequiredClass(serviceImplementation.toUp());
        }
        for (ApiSpecification endpointTechnology : requiredTechnologies) {
            builder.withRequiredTechnology(endpointTechnology.toUp());
        }
        for (ApiEndpointDefinition endpointDefinition : endpointDefinitions) {
            builder.withEndpointDefinition(endpointDefinition.toUp());
        }
        for (ApiServiceDefinition serviceDefinition : serviceDefinitions) {
            builder.withServiceDefinition(serviceDefinition.toUp());
        }
        return builder.build();
    }

    private List<ApiSpecification> requiredClasses = new ArrayList<>();
    private List<ApiSpecification> requiredTechnologies = new ArrayList<>();
    private List<ApiEndpointDefinition> endpointDefinitions = new ArrayList<>();
    private List<ApiServiceDefinition> serviceDefinitions = new ArrayList<>();

    public List<ApiSpecification> getRequiredClasses() {
        return requiredClasses;
    }

    public List<ApiSpecification> getRequiredTechnologies() {
        return requiredTechnologies;
    }

    public List<ApiEndpointDefinition> getEndpointDefinitions() {
        return endpointDefinitions;
    }

    public List<ApiServiceDefinition> getServiceDefinitions() {
        return serviceDefinitions;
    }

    @Override
    public String toString() {
        return String.format("ApiApplicationDefinition{%s, %s, %s, %s}",
                requiredClasses,
                requiredTechnologies,
                endpointDefinitions,
                serviceDefinitions);
    }
}
