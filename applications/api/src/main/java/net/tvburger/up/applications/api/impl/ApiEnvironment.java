package net.tvburger.up.applications.api.impl;

import net.tvburger.up.*;
import net.tvburger.up.applications.api.types.*;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.security.AccessDeniedException;

import javax.ws.rs.*;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ApiEnvironment extends ApiEntityManager<UpEnvironment.Manager> {

    private final UpEnvironment environment;

    ApiEnvironment(UpEnvironment environment) {
        this.environment = environment;
    }

    protected UpEnvironment.Manager getManager() throws AccessDeniedException {
        return environment.getManager();
    }

    @Path("/info")
    @GET
    public ApiEnvironmentInfo getInfo() {
        return ApiEnvironmentInfo.fromUp(environment.getInfo());
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

    @Path("/service_type/{serviceName}")
    @GET
    public ApiServiceInfo lookupService(@PathParam("serviceName") String serviceName) {
        return lookupService(serviceName, null);
    }

    @Path("/service_type/{serviceName}/{serviceVersion}")
    @GET
    public ApiServiceInfo lookupService(@PathParam("serviceName") String serviceName, @PathParam("serviceVersion") String serviceVersion) {
        for (UpService.Info<?> serviceInfo : environment.listServices()) {
            if (serviceInfo.getSpecificationName().equals(serviceName) &&
                    (serviceVersion == null || serviceInfo.getSpecificationVersion().equals(serviceVersion))) {
                try {
                    UpService.Manager<?> manager = environment.getServiceManager(serviceInfo);
                    if (environment.getManager().getState() == LifecycleManager.State.ACTIVE) {
                        return ApiServiceInfo.fromUp(serviceInfo);
                    }
                } catch (AccessDeniedException ignored) {
                }
            }
        }
        throw new NotFoundException();
    }

    @Path("/service")
    @GET
    public Set<ApiServiceInfo> listServices() {
        Set<ApiServiceInfo> services = new LinkedHashSet<>();
        for (UpService.Info<?> serviceInfo : environment.listServices()) {
            services.add(ApiServiceInfo.fromUp(serviceInfo));
        }
        return services;
    }

    @Path("/service/{uuid}")
    public ApiService getService(@PathParam("uuid") final String uuid) {
        UUID identificationUuid = UUID.fromString(uuid);
        for (UpService.Info<?> serviceInfo : environment.listServices()) {
            if (serviceInfo.getIdentification().getUuid().equals(identificationUuid)) {
                return new ApiService(environment, serviceInfo);
            }
        }
        throw new NotFoundException("No service with uuid: " + uuid);
    }

    @Path("/application")
    @GET
    public Set<ApiApplicationInfo> listApplications() {
        Set<ApiApplicationInfo> applicationInfoSet = new LinkedHashSet<>();
        for (UpApplication.Info applicationInfo : environment.listApplications()) {
            applicationInfoSet.add(ApiApplicationInfo.fromUp(applicationInfo));
        }
        return applicationInfoSet;
    }

    @Path("/application/{uuid}")
    public ApiApplication getApplication(@PathParam("uuid") final String uuid) throws AccessDeniedException {
        UUID identificationUuid = UUID.fromString(uuid);
        for (UpApplication.Info info : environment.listApplications()) {
            if (info.getIdentification().getUuid().equals(identificationUuid)) {
                return new ApiApplication(environment, environment.getApplication(info));
            }
        }
        throw new NotFoundException();
    }

    @Path("/package")
    @GET
    public Set<ApiPackageInfo> listPackages() {
        Set<ApiPackageInfo> packageInfoSet = new LinkedHashSet<>();
        for (UpPackage.Info info : environment.listPackages()) {
            packageInfoSet.add(ApiPackageInfo.fromUp(info));
        }
        return packageInfoSet;
    }

    @Path("/package/{uuid}")
    public ApiPackage getPackage(@PathParam("uuid") String uuid) {
        UUID packageId = UUID.fromString(uuid);
        for (UpPackage.Info packageInfo : listPackages()) {
            if (packageInfo.getPackageId().equals(packageId)) {
                return new ApiPackage(environment, packageInfo);
            }
        }
        throw new NotFoundException();
    }

    @Path("/endpoint")
    @GET
    public ApiList<ApiSpecification, Set<ApiEndpointInfo>> listEndpoints() {
        ApiList<ApiSpecification, Set<ApiEndpointInfo>> apiList = new ApiList<>();
        for (Map.Entry<Specification, Set<? extends UpEndpoint.Info>> entry : environment.listEndpoints().entrySet()) {
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
        for (Set<? extends UpEndpoint.Info> endpointInfoSet : environment.listEndpoints().values()) {
            for (UpEndpoint.Info endpointInfo : endpointInfoSet) {
                if (endpointInfo.getIdentification().getUuid().equals(identificationUuid)) {
                    return new ApiEndpoint(environment, endpointInfo);
                }
            }
        }
        throw new NotFoundException();
    }

    @Path("/deploy/package")
    @POST
    public UUID deployPackage(InputStream bytes) throws DeployException {
//        manager.deployPackage(packageDefinition);
//        try {
//            UpContext context = UpContext.getContext();
//            UpResourceRepository store = context.getRuntime().getFileStore(manager.getInfo());
//            UUID applicationUuid = UUID.randomUUID();
//            store.save(applicationDefinitionStream, applicationUuid);
//            File file = store.resolve(applicationUuid);
//            manager.deploy(ApiApplicationDefinition.Factory.createForEngine(file));
//        } catch (AccessDeniedException cause) {
//            throw new DeployException("Failed to deploy application: " + cause.getMessage(), cause);
//        }
        return null;
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
