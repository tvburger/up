package net.tvburger.up.runtime.context;

import java.io.Serializable;
import java.net.URI;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

public final class TransactionInfo implements Serializable {

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

    @Override
    public String toString() {
        return String.format("TransactionInfo{%s, %s, %s}", id, requestUri, requester);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) * 11 + 3;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof TransactionInfo)) {
            return false;
        }
        TransactionInfo other = (TransactionInfo) object;
        return Objects.equals(other.id, id)
                && Objects.equals(other.requester, requester)
                && Objects.equals(other.requestUri, requestUri);
    }

}
