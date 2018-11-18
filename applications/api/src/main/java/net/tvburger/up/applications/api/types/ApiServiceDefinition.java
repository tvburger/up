package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.topology.UpServiceDefinition;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiServiceDefinition {

    public static ApiServiceDefinition fromUp(UpServiceDefinition up) {
        ApiServiceDefinition api = new ApiServiceDefinition();
        api.serviceType = ApiSpecification.fromUp(up.getServiceType());
        api.instanceDefinition = ApiInstanceDefinition.fromUp(up.getInstanceDefinition());
        return api;
    }

    public UpServiceDefinition toUp() throws IOException {
        return UpServiceDefinition.Factory.create(serviceType.toUp(), instanceDefinition.toUp());
    }

    private ApiSpecification serviceType;
    private ApiInstanceDefinition instanceDefinition;

    public ApiSpecification getServiceType() {
        return serviceType;
    }

    public ApiInstanceDefinition getInstanceDefinition() {
        return instanceDefinition;
    }

}
