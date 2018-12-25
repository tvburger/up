package net.tvburger.up.runtime.context;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpService;
import net.tvburger.up.runtime.UpEngine;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class CallerInfo implements Serializable {

    public static final class Factory {

        public static CallerInfo create(final UpContext context) {
            Objects.requireNonNull(context);
            UpService<?> service = context.getService();
            UpEndpoint<?, ?> endpoint = context.getEndpoint();
            UpApplication application = context.getApplication();
            UpEngine engine = context.getEngine();
            return new CallerInfo(
                    service == null ? null : service.getInfo(),
                    endpoint == null ? null : endpoint.getInfo(),
                    application == null ? null : application.getInfo(),
                    engine == null ? null : engine.getInfo(),
                    context.getOperationId(),
                    System.currentTimeMillis());
        }

        private Factory() {
        }

    }

    private final UpService.Info<?> serviceInfo;
    private final UpEndpoint.Info endpointInfo;
    private final UpApplication.Info applicationInfo;
    private final UpEngine.Info engineInfo;
    private final UUID operationId;
    private final long timestamp;

    private CallerInfo(UpService.Info<?> serviceInfo, UpEndpoint.Info endpointInfo, UpApplication.Info applicationInfo, UpEngine.Info engineInfo, UUID operationId, long timestamp) {
        this.serviceInfo = serviceInfo;
        this.endpointInfo = endpointInfo;
        this.applicationInfo = applicationInfo;
        this.engineInfo = engineInfo;
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

    public UpApplication.Info getApplicationInfo() {
        return applicationInfo;
    }

    public UpEngine.Info getEngineInfo() {
        return engineInfo;
    }

    public UUID getOperationId() {
        return operationId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("CallerInfo{%s, %s, %s, %s, %s}", serviceInfo == null ? endpointInfo : serviceInfo, applicationInfo, engineInfo, operationId, timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(operationId) * 19 + Objects.hashCode(timestamp) * 3 + 29;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof CallerInfo)) {
            return false;
        }
        CallerInfo other = (CallerInfo) object;
        return Objects.equals(serviceInfo, other.serviceInfo)
                && Objects.equals(endpointInfo, other.endpointInfo)
                && Objects.equals(applicationInfo, other.applicationInfo)
                && Objects.equals(engineInfo, other.engineInfo)
                && Objects.equals(operationId, other.operationId)
                && Objects.equals(timestamp, other.timestamp);
    }

}
