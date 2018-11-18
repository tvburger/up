package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.topology.UpServiceDefinition;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiServiceDefinition {

    public static ApiServiceDefinition fromUp(UpServiceDefinition up) {
        ApiServiceDefinition api = new ApiServiceDefinition();
        api.serviceType = up.getServiceType();
        api.instanceDefinition = ApiInstanceDefinition.fromUp(up.getInstanceDefinition());
        return api;
    }

    public UpServiceDefinition toUp() throws IOException, ClassNotFoundException {
        return UpServiceDefinition.Factory.create(serviceType, instanceDefinition.toUp());
    }

    private Class<?> serviceType;
    private ApiInstanceDefinition instanceDefinition;

    public Class<?> getServiceType() {
        return serviceType;
    }

    public ApiInstanceDefinition getInstanceDefinition() {
        return instanceDefinition;
    }

}
