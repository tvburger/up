package net.tvburger.up.applications.api;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.ApiList;
import net.tvburger.up.applications.api.types.ApiServiceInfo;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public final class ApiApplication {

    private final UpApplication application;

    public ApiApplication(UpApplication application) {
        this.application = application;
    }

    @Path("/service")
    @GET
    public Set<ApiServiceInfo> listServices() {
        Set<ApiServiceInfo> services = new LinkedHashSet<>();
        for (UpService.Info<?> serviceInfo : application.listServices()) {
            services.add(ApiServiceInfo.fromUp(serviceInfo));
        }
        return services;
    }

    public <T> T getService(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return null;
    }

    @Path("/service/{uuid}")
    public ApiServiceManager getServiceManager(@PathParam("uuid") String uuid) throws AccessDeniedException {
        UUID serviceInstanceId = UUID.fromString(uuid);
        for (UpService.Info<?> serviceInfo : application.listServices()) {
            if (serviceInfo.getServiceInstanceId().equals(serviceInstanceId)) {
                return new ApiServiceManager(application.getServiceManager(serviceInfo));
            }
        }
        throw new NotFoundException();
    }

    @SuppressWarnings("unchecked")
    @Path("/endpoint")
    @GET
    public ApiList<Specification, Set<UpEndpoint.Info>> listEndpoints() {
        return new ApiList(application.listEndpoints());
    }

    public <I extends UpEndpoint.Info> UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException {
        return null;
    }

    @Path("/package")
    public ApiPackage getPackage() {
        return new ApiPackage(application.getPackage());
    }

    public Identification getIdentification() {
        return null;
    }

    @Path("/manager")
    public ApiApplicationManager getManager() throws AccessDeniedException {
        return new ApiApplicationManager(application.getManager());
    }

    @Path("/info")
    @GET
    public UpApplication.Info getInfo() {
        return application.getInfo();
    }

}
