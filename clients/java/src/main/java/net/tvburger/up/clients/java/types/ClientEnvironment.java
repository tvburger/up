package net.tvburger.up.clients.java.types;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.clients.java.impl.ApiRequester;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.Map;
import java.util.Set;

public final class ClientEnvironment extends ApiRequester implements UpEnvironment {

    private static final ResponseType listServiceTypesResponseType =
            new ResponseType.Set(
                    new ResponseType.Value(Class.class));

    private static final ResponseType listServicesResponseType =
            new ResponseType.Set(
                    new ResponseType.Value(ClientServiceInfo.class));

    private static final ResponseType listEndpointsResponseType =
            new ResponseType.MapList(
                    new ResponseType.Value(ClientSpecification.class),
                    new ResponseType.Set(new ResponseType.Value(ClientEndpointInfo.class)));

    public ClientEnvironment(ApiRequester requester) {
        super(requester);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Class<?>> listServiceTypes() {
        try {
            return request("service_type", listServiceTypesResponseType);
        } catch (ApiException cause) {
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
            return request("service", listServicesResponseType);
        } catch (ApiException cause) {
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
            return request("endpoint", listEndpointsResponseType);
        } catch (ApiException cause) {
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
            return request("identification", ClientIdentification.class);
        } catch (ApiException cause) {
            throw new ApiException("Failed to read identification: " + cause.getMessage(), cause);
        }
    }

    @Override
    public Manager getManager() throws AccessDeniedException {
        // TODO: return API service proxy
        return null;
    }

    @Override
    public Info getInfo() {
        try {
            return request("info", ClientEnvironmentInfo.class);
        } catch (ApiException cause) {
            throw new ApiException("Failed to read environment info: " + cause.getMessage(), cause);
        }
    }

}
