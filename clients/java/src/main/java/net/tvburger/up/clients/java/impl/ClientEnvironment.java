package net.tvburger.up.clients.java.impl;

import net.tvburger.up.*;
import net.tvburger.up.applications.api.types.*;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class ClientEnvironment extends ApiRequester implements UpEnvironment {

    private static final ApiResponseType listServiceTypesResponseType =
            new ApiResponseType.Set(
                    new ApiResponseType.Value(ApiClass.class));

    private static final ApiResponseType listServicesResponseType =
            new ApiResponseType.Set(
                    new ApiResponseType.Value(ApiServiceInfo.class));

    private static final ApiResponseType listEndpointsResponseType =
            new ApiResponseType.MapList(
                    new ApiResponseType.Value(ApiSpecification.class),
                    new ApiResponseType.Set(new ApiResponseType.Value(ApiEndpointInfo.class)));

    private static final ApiResponseType listPackagesResponseType =
            new ApiResponseType.Set(
                    new ApiResponseType.Value(ApiPackageInfo.class));

    private static final ApiResponseType listApplicationsResponseType =
            new ApiResponseType.Set(
                    new ApiResponseType.Value(ApiApplicationInfo.class));

    public ClientEnvironment(ApiRequester requester) {
        super(requester);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Class<?>> listServiceTypes() {
        try {
            Set<Class<?>> serviceTypes = new LinkedHashSet<>();
            for (ApiClass apiClass : (Set<ApiClass>) apiRead("service_type", listServiceTypesResponseType)) {
                serviceTypes.add(apiClass.toUp(null));
            }
            return serviceTypes;
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to read list service types: " + cause.getMessage(), cause);
        }
    }

    @Override
    public <T> T lookupService(Class<T> serviceType) {
        // TODO: return API service proxy
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<UpService.Info<?>> listServices() {
        try {
            Set<UpService.Info<?>> services = new LinkedHashSet<>();
            for (ApiServiceInfo apiServiceInfo : (Set<ApiServiceInfo>) apiRead("service", listServicesResponseType)) {
                services.add(apiServiceInfo.toUp());
            }
            return services;
        } catch (UpException | ClassNotFoundException | ApiException cause) {
            throw new ApiException("Failed to read list services: " + cause.getMessage(), cause);
        }
    }

    @Override
    public <T> T getService(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        // TODO: return API service proxy
        return null;
    }

    @Override
    public <T> UpService.Manager<T> getServiceManager(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return new ClientServiceManager<>("service/" + serviceInfo.getServiceInstanceId(), this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Specification, Set<? extends UpEndpoint.Info>> listEndpoints() {
        try {
            return apiRead("endpoint", listEndpointsResponseType);
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to read list endpoints: " + cause.getMessage(), cause);
        }
    }

    @Override
    public <I extends UpEndpoint.Info> UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException {
        // TODO: return API service proxy
        return null;
    }

    @Override
    public Set<UpPackage.Info> listPackages() {
        try {
            return apiRead("package", listPackagesResponseType);
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to read list packages: " + cause.getMessage(), cause);
        }
    }

    @Override
    public UpPackage.Manager getPackageManager(UpPackage.Info packageInfo) throws AccessDeniedException {
        // TODO: need to specifcy packageId...
        return new ClientPackageManager(this);
    }

    @Override
    public Set<UpApplication.Info> listApplications() {
        try {
            return apiRead("application", listApplicationsResponseType);
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to read list applications: " + cause.getMessage(), cause);
        }
    }

    @Override
    public UpApplication getApplication(UpApplication.Info applicationInfo) throws AccessDeniedException {
        return new ClientApplication(applicationInfo.getName(), this);
    }

    @Override
    public Identification getIdentification() {
        try {
            return apiRead("identification", ApiIdentification.class);
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to read identification: " + cause.getMessage(), cause);
        }
    }

    @Override
    public Manager getManager() throws AccessDeniedException {
//        try {
//            apiRead("manager/info");
        return new ClientEnvironmentManager(this);
//        } catch (AccessDeniedException cause) {
//            throw cause;
//        } catch (UpException | ApiException cause) {
//            throw new ApiException("Failed to read manager: " + cause.getMessage(), cause);
//        }
    }

    @Override
    public Info getInfo() {
        try {
            return apiRead("info", ApiEnvironmentInfo.class);
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to read environment info: " + cause.getMessage(), cause);
        }
    }

}
