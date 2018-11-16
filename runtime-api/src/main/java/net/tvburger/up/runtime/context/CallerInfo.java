package net.tvburger.up.runtime.context;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpService;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class CallerInfo implements Serializable {

    public static final class Factory {

        public static CallerInfo create() {
            return new CallerInfo(
                    null,
                    null,
                    UUID.randomUUID(),
                    System.currentTimeMillis());
        }

        public static CallerInfo create(UpContext context) {
            Objects.requireNonNull(context);
            UpService<?> service = context.getService();
            UpEndpoint<?, ?> endpoint = context.getEndpoint();
            return new CallerInfo(
                    service == null ? null : service.getInfo(),
                    endpoint == null ? null : endpoint.getInfo(),
                    context.getOperationId(),
                    System.currentTimeMillis());
        }

        public static CallerInfo create(UpEndpoint.Info endpointInfo, UUID operationId) {
            Objects.requireNonNull(endpointInfo);
            Objects.requireNonNull(operationId);
            return new CallerInfo(null, endpointInfo, operationId, System.currentTimeMillis());
        }

        public static CallerInfo create(UpService.Info<?> serviceInfo, UUID operationId) {
            Objects.requireNonNull(serviceInfo);
            Objects.requireNonNull(operationId);
            return new CallerInfo(serviceInfo, null, operationId, System.currentTimeMillis());
        }

        private Factory() {
        }

    }

    private final UpService.Info<?> serviceInfo;
    private final UpEndpoint.Info endpointInfo;
    private final UUID operationId;
    private final long timestamp;

    private CallerInfo(UpService.Info<?> serviceInfo, UpEndpoint.Info endpointInfo, UUID operationId, long timestamp) {
        this.serviceInfo = serviceInfo;
        this.endpointInfo = endpointInfo;
        this.operationId = operationId;
        this.timestamp = timestamp;
    }

    /**
     * Returns null if NOT called from a UpService
     *
     * @return
     */
    public UpService.Info<?> getServiceInfo() {
        return serviceInfo;
    }

    /**
     * Returns null if NOT called from an UpEndpoint
     *
     * @return
     */
    public UpEndpoint.Info getEndpointInfo() {
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
        return String.format("CallerInfo{%s, %s, %s}", serviceInfo == null ? endpointInfo : serviceInfo, operationId, timestamp);
    }

}
