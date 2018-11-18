package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.*;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.Map;
import java.util.Set;

public final class ClientEnvironment extends ApiRequester implements UpEnvironment {

    private static final ApiResponseType listServiceTypesResponseType =
            new ApiResponseType.Set(
                    new ApiResponseType.Value(Class.class));

    private static final ApiResponseType listServicesResponseType =
            new ApiResponseType.Set(
                    new ApiResponseType.Value(ApiServiceInfo.class));

    private static final ApiResponseType listEndpointsResponseType =
            new ApiResponseType.MapList(
                    new ApiResponseType.Value(ApiSpecification.class),
                    new ApiResponseType.Set(new ApiResponseType.Value(ApiEndpointInfo.class)));

    public ClientEnvironment(ApiRequester requester) {
        super(requester);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Class<?>> listServiceTypes() {
        try {
            return apiRead("service_type", listServiceTypesResponseType);
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
            return apiRead("service", listServicesResponseType);
        } catch (UpException | ApiException cause) {
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
        // TODO: return API service proxy
        return null;
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
