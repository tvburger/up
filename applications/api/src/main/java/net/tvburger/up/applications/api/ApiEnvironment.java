package net.tvburger.up.applications.api;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.ApiList;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Set;

public final class ApiEnvironment {

    private final UpEnvironment environment;

    public ApiEnvironment(UpEnvironment environment) {
        this.environment = environment;
    }

    @Path("/service_type")
    @GET
    public Set listServiceTypes() {
        return environment.listServiceTypes();
    }

    public <T> T lookupService(Class<T> serviceType) {
        return null;
    }

    @Path("/service")
    @GET
    public Set listServices() {
        return environment.listServices();
    }

    public <T> T getService(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return null;
    }

    @Path("/service/{name}")
    public ApiServiceManager getServiceManager(UpService.Info<?> serviceInfo) throws AccessDeniedException {
        return new ApiServiceManager(environment.getServiceManager(serviceInfo));
    }

    @SuppressWarnings("unchecked")
    @Path("/endpoint")
    @GET
    public ApiList listEndpoints() {
        return new ApiList(environment.listEndpoints());
    }

    public <I extends UpEndpoint.Info> UpEndpoint.Manager getEndpointManager(I endpointInfo) throws AccessDeniedException {
        return null;
    }

    @Path("/manager")
    public ApiEnvironmentManager getManager() throws AccessDeniedException {
        return new ApiEnvironmentManager(environment.getManager());
    }

    @Path("/info")
    @GET
    public UpEnvironment.Info getInfo() {
        return environment.getInfo();
    }

    @Path("/identification")
    @GET
    public Identification getIdentification() {
        return environment.getIdentification();
    }

}
