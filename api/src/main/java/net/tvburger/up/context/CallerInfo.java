package net.tvburger.up.context;

import net.tvburger.up.EndpointInfo;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.client.UpClientInfo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class CallerInfo implements Serializable {

    public static final class Factory {

        public static CallerInfo create(UpContext context) {
            Objects.requireNonNull(context);
            return new CallerInfo(context.getServiceInfo(), context.getCallerInfo().getClientInfo(), null, UUID.randomUUID(), System.currentTimeMillis());
        }

        public static CallerInfo create(EndpointInfo endpointInfo) {
            Objects.requireNonNull(endpointInfo);
            return new CallerInfo(null, null, endpointInfo, UUID.randomUUID(), System.currentTimeMillis());
        }

        public static CallerInfo create(ServiceInfo<?> serviceInfo) {
            Objects.requireNonNull(serviceInfo);
            return new CallerInfo(serviceInfo, null, null, UUID.randomUUID(), System.currentTimeMillis());
        }

        public static CallerInfo create(UpClientInfo clientInfo) {
            Objects.requireNonNull(clientInfo);
            return new CallerInfo(null, clientInfo, null, UUID.randomUUID(), System.currentTimeMillis());
        }

        private Factory() {
        }

    }

    private final ServiceInfo<?> serviceInfo;
    private final UpClientInfo clientInfo;
    private final EndpointInfo endpointInfo;
    private final UUID operationId;
    private final long timestamp;

    private CallerInfo(ServiceInfo<?> serviceInfo, UpClientInfo clientInfo, EndpointInfo endpointInfo, UUID operationId, long timestamp) {
        this.serviceInfo = serviceInfo;
        this.clientInfo = clientInfo;
        this.endpointInfo = endpointInfo;
        this.operationId = operationId;
        this.timestamp = timestamp;
    }

    /**
     * Returns null if NOT called from a Service
     *
     * @return
     */
    public ServiceInfo<?> getServiceInfo() {
        return serviceInfo;
    }

    /**
     * Returns null if NOT called from a Client
     *
     * @return
     */
    public UpClientInfo getClientInfo() {
        return clientInfo;
    }

    /**
     * Returns null if NOT called from an Endpoint
     *
     * @return
     */
    public EndpointInfo getEndpointInfo() {
        return endpointInfo;
    }

    public UUID getOperationId() {
        return operationId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("CallerInfo{%s, %s, %s, %s, %s}", serviceInfo, clientInfo, endpointInfo, operationId, timestamp);
    }

}
