package net.tvburger.up.applications.api;

import net.tvburger.up.*;
import net.tvburger.up.applications.api.types.ApiClass;
import net.tvburger.up.applications.api.types.ApiList;
import net.tvburger.up.applications.api.types.ApiServiceInfo;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.*;

public final class ApiEnvironment {

    private final UpEnvironment environment;
    private final Map<UUID, ApiServiceProxy> services = new HashMap<>();

    public ApiEnvironment(UpEnvironment environment) {
        this.environment = environment;
    }

    @Path("/service_type")
    @GET
    public Set<ApiClass> listServiceTypes() {
        Set<ApiClass> serviceTypes = new LinkedHashSet<>();
        for (Class<?> serivceType : environment.listServiceTypes()) {
            serviceTypes.add(ApiClass.fromUp(serivceType));
        }
        return serviceTypes;
    }

//    public UUID lookupService(Api serviceType) {
//        return environment.lookupService(serviceType);
//    }

    @Path("/service")
    @GET
    public Set<ApiServiceInfo> listServices() {
        Set<ApiServiceInfo> services = new LinkedHashSet<>();
        for (UpService.Info<?> serviceInfo : environment.listServices()) {
            services.add(ApiServiceInfo.fromUp(serviceInfo));
        }
        System.out.println(services);
        return services;
    }

    public <T> T getService(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return null;
    }

    @Path("/service/{uuid}")
    public ApiServiceManager getServiceManager(@PathParam("uuid") String uuid) throws AccessDeniedException, UpRuntimeException {
        UUID serviceInstanceId = UUID.fromString(uuid);
        for (UpService.Info<?> info : environment.listServices()) {
            if (info.getServiceInstanceId().equals(serviceInstanceId)) {
                return new ApiServiceManager(environment.getServiceManager(info));
            }
        }
        throw new NotFoundException();
    }

    @SuppressWarnings("unchecked")
    @Path("/endpoint")
    @GET
    public ApiList<Specification, Set<UpEndpoint.Info>> listEndpoints() {
        return new ApiList(environment.listEndpoints());
    }

    //    public <I extends UpEndpoint.Info> UpEndpoint.Manager getEndpointManager(I endpointInfo) throws AccessDeniedException {
//        return null;
//    }

    @Path("/package")
    @GET
    public Set<UpPackage.Info> listPackages() {
        return environment.listPackages();
    }

    @Path("/application")
    @GET
    public Set<UpApplication.Info> listApplications() {
        return environment.listApplications();
    }

    @Path("/application/{name}")
    public ApiApplication getApplication(@PathParam("name") String name) throws AccessDeniedException {
        for (UpApplication.Info info : environment.listApplications()) {
            if (info.getName().equals(name)) {
                return new ApiApplication(environment.getApplication(info));
            }
        }
        throw new NotFoundException();
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
