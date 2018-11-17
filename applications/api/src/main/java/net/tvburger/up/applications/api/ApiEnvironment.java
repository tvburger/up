package net.tvburger.up.applications.api;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.util.UpEnvironments;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Set;

@Path("/{environmentName}")
public final class ApiEnvironment {

    @PathParam("environmentName")
    private String environmentName;

    private UpEnvironment getEnvironment() {
        try {
            return UpEnvironments.get(environmentName);
        } catch (Throwable b) {
            throw new RuntimeException(b);
        }
    }

    @Path("/service_type")
    @GET
    public Set<Class<?>> listServiceTypes() {
        return getEnvironment().listServiceTypes();
    }

    public <T> T lookupService(Class<T> serviceType) {
        return null;
    }

    @Path("/service")
    @GET
    public Set<UpService.Info<?>> listServices() {
        return getEnvironment().listServices();
    }

    public <T> T getService(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return null;
    }

    public <T> UpService.Manager<T> getServiceManager(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return null;
    }

    @Path("/endpoint")
    @GET
    public ApiList<Specification, Set<? extends UpEndpoint.Info>> listEndpoints() {
        return new ApiList<>(getEnvironment().listEndpoints());
    }

    public <I extends UpEndpoint.Info> UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException {
        return null;
    }

    public UpEnvironment.Manager getManager() throws AccessDeniedException {
        return null;
    }

    @Path("/info")
    @GET
    public UpEnvironment.Info getInfo() {
        return getEnvironment().getInfo();
    }

    @Path("/identification")
    @GET
    public Identification getIdentification() {
        return getEnvironment().getIdentification();
    }

}
