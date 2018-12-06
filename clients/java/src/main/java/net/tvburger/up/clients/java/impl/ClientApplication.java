package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpException;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.ApiApplicationInfo;
import net.tvburger.up.applications.api.types.ApiEndpointInfo;
import net.tvburger.up.applications.api.types.ApiServiceInfo;
import net.tvburger.up.applications.api.types.ApiSpecification;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class ClientApplication extends ApiRequester implements UpApplication {

    private static final ApiResponseType listServicesResponseType =
            new ApiResponseType.Set(
                    new ApiResponseType.Value(ApiServiceInfo.class));

    private static final ApiResponseType listEndpointsResponseType =
            new ApiResponseType.MapList(
                    new ApiResponseType.Value(ApiSpecification.class),
                    new ApiResponseType.Set(new ApiResponseType.Value(ApiEndpointInfo.class)));

    public ClientApplication(String name, ApiRequester requester) {
        super(requester, "application/" + name);
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
        return null;
    }

    @Override
    public <T> UpService.Manager<T> getServiceManager(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return new ClientServiceManager<>("service/" + serviceInfo.getServiceInstanceId() + "/manager", this);
    }

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
        return null;
    }

    @Override
    public Identification getIdentification() {
        return null;
    }

    @Override
    public Manager getManager() throws AccessDeniedException {
        return new ClientApplicationManager(this);
    }

    @Override
    public Info getInfo() {
        try {
            return apiRead("info", ApiApplicationInfo.class);
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to read application info: " + cause.getMessage(), cause);
        }
    }

}
