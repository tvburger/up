package net.tvburger.up.clients.java.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.security.Identity;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
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

    public interface ResponseType {

        final class MapList implements ResponseType {

            private final ResponseType keyType;
            private final ResponseType valueType;

            public MapList(ResponseType keyType, ResponseType valueType) {
                this.keyType = keyType;
                this.valueType = valueType;
            }

            public ResponseType getKeyType() {
                return keyType;
            }

            public ResponseType getValueType() {
                return valueType;
            }

            @Override
            public Class<?> getType() {
                return LinkedList.class;
            }

        }

        final class Map implements ResponseType {

            private final ResponseType keyType;
            private final ResponseType valueType;

            public Map(ResponseType keyType, ResponseType valueType) {
                this.keyType = keyType;
                this.valueType = valueType;
            }

            public ResponseType getKeyType() {
                return keyType;
            }

            public ResponseType getValueType() {
                return valueType;
            }

            @Override
            public Class<?> getType() {
                return LinkedHashMap.class;
            }

        }

        final class Set implements ResponseType {

            private final ResponseType innerType;

            public Set(ResponseType innerType) {
                this.innerType = innerType;
            }

            public ResponseType getInnerType() {
                return innerType;
            }

            @Override
            public Class<?> getType() {
                return LinkedHashSet.class;
            }

        }

        final class Value implements ResponseType {

            private final Class<?> valueType;

            public Value(Class<?> valueType) {
                this.valueType = valueType;
            }

            @Override
            public Class<?> getType() {
                return valueType;
            }

        }

        Class<?> getType();

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

    protected <T> T request(String path, Class<T> responseType) throws ApiException {
        return request(path, new ResponseType.Value(responseType));
    }

    @SuppressWarnings("unchecked")
    protected <T> T request(String path, ResponseType responseType) throws ApiException {
        Objects.requireNonNull(path);
        Objects.requireNonNull(responseType);
        try {
            String stringResponse = client.target(factory.createTarget(path)).request()
                    .accept(MediaType.APPLICATION_XML_TYPE)
                    .header("Client-Name", identity.getPrincipal())
                    .get(String.class);
            return (T) parse(stringResponse, responseType);
        } catch (ProcessingException | WebApplicationException cause) {
            throw new ApiException("Failed to query: " + path + ", because of: " + cause.getMessage(), cause);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T parse(Object rawResponse, Class<T> responseType) throws ApiException {
        return (T) parse(rawResponse, new ResponseType.Value(responseType));
    }

    @SuppressWarnings("unchecked")
    protected Object parse(Object rawResponse, ResponseType responseType) throws ApiException {
        try {
            String stringResponse = rawResponse instanceof String ? (String) rawResponse : mapper.writeValueAsString(rawResponse);
            Objects.requireNonNull(responseType);
            Object response;
            if (responseType instanceof ResponseType.Value) {
                if (Class.class.isAssignableFrom(responseType.getType())) {
                    response = provider.getClass(stringResponse);
                } else {
                    response = mapper.readValue(stringResponse, responseType.getType());
                }
            } else if (responseType instanceof ResponseType.Set) {
                Set set = new LinkedHashSet();
                for (Object item : ((Set) mapper.readValue(stringResponse, responseType.getType()))) {
                    set.add(parse(item, ((ResponseType.Set) responseType).getInnerType()));
                }
                response = set;
            } else if (responseType instanceof ResponseType.Map) {
                Map map = new LinkedHashMap();
                for (Map.Entry entry : (Set<Map.Entry>) ((Map) mapper.readValue(stringResponse, responseType.getType())).entrySet()) {
                    map.put(
                            parse(entry.getKey(), ((ResponseType.Map) responseType).getKeyType()),
                            parse(entry.getValue(), ((ResponseType.Map) responseType).getValueType()));
                }
                response = map;
            } else if (responseType instanceof ResponseType.MapList) {
                Map map = new LinkedHashMap();
                for (Map entry : (List<Map>) mapper.readValue(stringResponse, responseType.getType())) {
                    map.put(
                            parse(entry.get("key"), ((ResponseType.MapList) responseType).getKeyType()),
                            parse(entry.get("value"), ((ResponseType.MapList) responseType).getValueType()));
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
