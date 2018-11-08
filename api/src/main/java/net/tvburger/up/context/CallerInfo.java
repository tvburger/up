package net.tvburger.up.context;

import net.tvburger.up.ServiceInfo;
import net.tvburger.up.client.UpClientInfo;

import java.io.Serializable;
import java.util.Objects;

public final class CallerInfo implements Serializable {

    public static final class Factory {

        public static CallerInfo create() {
            return new CallerInfo(null, null, System.currentTimeMillis());
        }

        public static CallerInfo create(UpContext context) {
            Objects.requireNonNull(context);
            return new CallerInfo(context.getServiceInfo(), context.getCallerInfo().getClientInfo(), System.currentTimeMillis());
        }

        public static CallerInfo create(ServiceInfo<?> serviceInfo) {
            Objects.requireNonNull(serviceInfo);
            return new CallerInfo(serviceInfo, null, System.currentTimeMillis());
        }

        public static CallerInfo create(UpClientInfo clientInfo) {
            Objects.requireNonNull(clientInfo);
            return new CallerInfo(null, clientInfo, System.currentTimeMillis());
        }

        private Factory() {
        }

    }

    private final ServiceInfo<?> serviceInfo;
    private final UpClientInfo clientInfo;
    private final long timestamp;

    private CallerInfo(ServiceInfo<?> serviceInfo, UpClientInfo clientInfo, long timestamp) {
        this.serviceInfo = serviceInfo;
        this.clientInfo = clientInfo;
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

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("CallerInfo{%s, %s, %s}", serviceInfo, clientInfo, timestamp);
    }

}
