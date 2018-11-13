package net.tvburger.up.context;

import java.net.URI;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

public final class TransactionInfo {

    public static final class Factory {

        public static TransactionInfo create(URI endpointUri) {
            return create(null, endpointUri);
        }

        public static TransactionInfo create(Principal requester, URI requestUri) {
            Objects.requireNonNull(requestUri);
            return new TransactionInfo(UUID.randomUUID(), requestUri, requester);
        }

        private Factory() {
        }

    }

    private final UUID id;
    private final URI requestUri;
    private final Principal requester;

    private TransactionInfo(UUID id, URI requestUri, Principal requester) {
        this.id = id;
        this.requestUri = requestUri;
        this.requester = requester;
    }

    public UUID getId() {
        return id;
    }

    public URI getRequestUri() {
        return requestUri;
    }

    public Principal getRequester() {
        return requester;
    }

}
