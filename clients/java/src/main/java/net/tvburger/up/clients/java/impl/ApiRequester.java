package net.tvburger.up.clients.java.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.client.UpClientException;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public abstract class ApiRequester {

    public static final class Factory {

        public static ApiRequester create(Client client, String target, Identity identity) throws MalformedURLException {
            Objects.requireNonNull(client);
            Objects.requireNonNull(target);
            Objects.requireNonNull(identity);
            return new ApiRequester(client, new TargetFactory(target), identity, ApiClassProvider.Factory.create(target)) {
            };
        }

        private Factory() {
        }

    }

    private static final class TargetFactory {

        private final String baseUrl;

        private TargetFactory(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        private String createTarget(String target) {
            return baseUrl + "/" + target;
        }

        private TargetFactory createSub(String target) {
            return new TargetFactory(createTarget(target));
        }

    }

    private static final ObjectMapper mapper = new ObjectMapper();

    private final Client client;
    private final TargetFactory factory;
    private final Identity identity;
    private final ApiClassProvider provider;

    protected ApiRequester(ApiRequester requester) {
        this(requester.client, requester.factory, requester.identity, requester.provider);
    }

    protected ApiRequester(ApiRequester requester, String target) {
        this(requester.client, requester.factory.createSub(target), requester.identity, requester.provider);
    }

    private ApiRequester(Client client, TargetFactory factory, Identity identity, ApiClassProvider provider) {
        this.client = client;
        this.factory = factory;
        this.identity = identity;
        this.provider = provider;
    }

    protected void apiWrite(String path) throws ApiException, UpClientException, DeployException, AccessDeniedException, UpRuntimeException, LifecycleException {
        doPostRequest(path, null);
    }

    protected void apiWrite(String path, Object payload) throws ApiException, UpClientException, DeployException, AccessDeniedException, UpRuntimeException, LifecycleException {
        doPostRequest(path, payload);
    }

    private Response doPostRequest(String path, Object payload) throws ApiException, UpClientException, AccessDeniedException, DeployException, UpRuntimeException, LifecycleException {
        Objects.requireNonNull(path);
        try {
            Response response = client.target(factory.createTarget(path)).request()
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .header("Client-Name", identity.getPrincipal())
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON_TYPE));
            handleStatus(response);
            return response;
        } catch (ProcessingException cause) {
            throw new ApiException("Failed to post: " + path + ", because of: " + cause.getMessage(), cause);
        }
    }

    private void handleStatus(Response response) throws AccessDeniedException, DeployException, UpClientException, ApiException, UpRuntimeException, LifecycleException {
        switch (response.getStatusInfo().getFamily()) {
            case INFORMATIONAL:
            case SUCCESSFUL:
                break;
            case REDIRECTION:
                throw new ApiException("Failed due to redirection!");
            case CLIENT_ERROR:
                switch (response.getStatusInfo().toEnum()) {
                    case UNAUTHORIZED:
                        throw new AccessDeniedException();
                    case FORBIDDEN:
                        throw new DeployException();
                    case CONFLICT:
                        throw new LifecycleException();
                    case BAD_REQUEST:
                        throw new UpClientException();
                    default:
                        throw new ApiException(response.getStatusInfo().getReasonPhrase());
                }
            case SERVER_ERROR:
                throw new UpRuntimeException();
            case OTHER:
            default:
                throw new ApiException("Unexpected error: " + response.getStatusInfo().getReasonPhrase());
        }
    }

    protected void apiRead(String path) throws ApiException, AccessDeniedException, DeployException, UpClientException, UpRuntimeException, LifecycleException {
        doGetRequest(path);
    }

    protected <T> T apiRead(String path, Class<T> responseType) throws ApiException, AccessDeniedException, DeployException, UpClientException, UpRuntimeException, LifecycleException {
        return apiRead(path, new ApiResponseType.Value(responseType));
    }

    @SuppressWarnings("unchecked")
    protected <T> T apiRead(String path, ApiResponseType responseType) throws ApiException, AccessDeniedException, DeployException, UpClientException, UpRuntimeException, LifecycleException {
        Objects.requireNonNull(path);
        Objects.requireNonNull(responseType);
        try {
            String stringResponse = doGetRequest(path).readEntity(String.class);
            return (T) parse(stringResponse, responseType);
        } catch (ProcessingException | WebApplicationException cause) {
            throw new ApiException("Failed to query: " + path + ", because of: " + cause.getMessage(), cause);
        }
    }

    private Response doGetRequest(String path) throws ApiException, AccessDeniedException, DeployException, UpClientException, UpRuntimeException, LifecycleException {
        Objects.requireNonNull(path);
        try {
            Response response = client.target(factory.createTarget(path)).request()
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .header("Client-Name", identity.getPrincipal())
                    .get();
            handleStatus(response);
            return response;
        } catch (ProcessingException cause) {
            throw new ApiException("Failed to get: " + path + ", because of: " + cause.getMessage(), cause);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T parse(Object rawResponse, Class<T> responseType) throws ApiException {
        return (T) parse(rawResponse, new ApiResponseType.Value(responseType));
    }

    @SuppressWarnings("unchecked")
    protected Object parse(Object rawResponse, ApiResponseType responseType) throws ApiException {
        try {
            String stringResponse = rawResponse instanceof String ? (String) rawResponse : mapper.writeValueAsString(rawResponse);
            Objects.requireNonNull(responseType);
            Object response;
            if (responseType instanceof ApiResponseType.Value) {
                if (Class.class.isAssignableFrom(responseType.getType())) {
                    response = provider.getClass(stringResponse);
                } else if (Enum.class.isAssignableFrom(responseType.getType())) {
                    response = Enum.valueOf((Class) responseType.getType(), stringResponse);
                } else {
                    response = mapper.readValue(stringResponse, responseType.getType());
                }
            } else if (responseType instanceof ApiResponseType.Set) {
                Set set = new LinkedHashSet();
                for (Object item : ((Set) mapper.readValue(stringResponse, responseType.getType()))) {
                    set.add(parse(item, ((ApiResponseType.Set) responseType).getInnerType()));
                }
                response = set;
            } else if (responseType instanceof ApiResponseType.Map) {
                Map map = new LinkedHashMap();
                for (Map.Entry entry : (Set<Map.Entry>) ((Map) mapper.readValue(stringResponse, responseType.getType())).entrySet()) {
                    map.put(
                            parse(entry.getKey(), ((ApiResponseType.Map) responseType).getKeyType()),
                            parse(entry.getValue(), ((ApiResponseType.Map) responseType).getValueType()));
                }
                response = map;
            } else if (responseType instanceof ApiResponseType.MapList) {
                Map map = new LinkedHashMap();
                for (Map entry : (List<Map>) mapper.readValue(stringResponse, responseType.getType())) {
                    map.put(
                            parse(entry.get("key"), ((ApiResponseType.MapList) responseType).getKeyType()),
                            parse(entry.get("value"), ((ApiResponseType.MapList) responseType).getValueType()));
                }
                response = map;
            } else {
                throw new ApiException("Invalid responseType: " + responseType);
            }
            return response;
        } catch (IOException cause) {
            throw new ApiException("Failed to parse response: " + cause.getMessage(), cause);
        }
    }

}
