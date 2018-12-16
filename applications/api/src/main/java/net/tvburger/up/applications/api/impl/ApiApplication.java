package net.tvburger.up.applications.api.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.*;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.security.AccessDeniedException;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ApiApplication extends ApiEntityManager<UpApplication.Manager> {

    private final UpEnvironment environment;
    private final UpApplication application;

    ApiApplication(final UpEnvironment environment, final UpApplication application) {
        this.environment = environment;
        this.application = application;
    }

    protected final UpApplication.Manager getManager() throws AccessDeniedException {
        return application.getManager();
    }

    @Path("/info")
    @GET
    public ApiApplicationInfo getInfo() {
        return ApiApplicationInfo.fromUp(application.getInfo());
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

    @Path("/service/{uuid}")
    public ApiService getService(@PathParam("uuid") String uuid) {
        UUID identificationUuid = UUID.fromString(uuid);
        for (UpService.Info<?> serviceInfo : application.listServices()) {
            if (serviceInfo.getIdentification().getUuid().equals(identificationUuid)) {
                return new ApiService(environment, serviceInfo);
            }
        }
        throw new NotFoundException();
    }

    @Path("/endpoint")
    @GET
    public ApiList<ApiSpecification, Set<ApiEndpointInfo>> listEndpoints() {
        ApiList<ApiSpecification, Set<ApiEndpointInfo>> apiList = new ApiList<>();
        for (Map.Entry<Specification, Set<? extends UpEndpoint.Info>> entry : application.listEndpoints().entrySet()) {
            Set<ApiEndpointInfo> environmentInfoSet = new LinkedHashSet<>();
            for (UpEndpoint.Info endpointInfo : entry.getValue()) {
                environmentInfoSet.add(ApiEndpointInfo.fromUp(endpointInfo));
            }
            apiList.add(ApiSpecification.fromUp(entry.getKey()), environmentInfoSet);
        }
        return apiList;
    }

    @Path("/endpoint/{uuid}")
    public ApiEndpoint getEndpoint(@PathParam("uuid") String uuid) {
        UUID identificationUuid = UUID.fromString(uuid);
        for (Set<? extends UpEndpoint.Info> endpointInfoSet : application.listEndpoints().values()) {
            for (UpEndpoint.Info endpointInfo : endpointInfoSet) {
                if (endpointInfo.getIdentification().getUuid().equals(identificationUuid)) {
                    return new ApiEndpoint(environment, endpointInfo);
                }
            }
        }
        throw new NotFoundException();
    }

    // TODO: support application, serivce and endpoint definitions (?)
//    @Path("/deploy/service")
//    @POST
//    public void deploy(ApiServiceDefinition serviceDefinition, ApiApplicationDefinition applicationDefinition) throws DeployException {
//        try {
//            manager.deploy(serviceDefinition.toUp(), applicationDefinition);
//        } catch (IOException cause) {
//            throw new DeployException("Failed to deploy: " + cause.getMessage(), cause);
//        }
//    }
//
//    @Path("/deploy/endpoint")
//    @POST
//    public void deploy(ApiEndpointDefinition endpointDefinition, ApiApplicationDefinition applicationDefinition) throws DeployException {
//        try {
//            manager.deploy(endpointDefinition.toUp(), applicationDefinition);
//        } catch (IOException cause) {
//            throw new DeployException("Failed to deploy: " + cause.getMessage(), cause);
//        }
//    }

}
