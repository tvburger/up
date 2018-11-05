package net.tvburger.up.service;

import net.tvburger.up.ServiceInfo;

import java.io.Serializable;

public class CallerInfo implements Serializable {

    public static final class Builder {

        private StackTraceElement source;
        private InfraTraceElement location;
        private ServiceInfo serviceInfo;
        private Long timestamp;

        public Builder withSource(StackTraceElement source) {
            this.source = source;
            return this;
        }

        public Builder withLocation(InfraTraceElement location) {
            this.location = location;
            return this;
        }

        public Builder withServiceInfo(ServiceInfo serviceInfo) {
            this.serviceInfo = serviceInfo;
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

    }

    private final StackTraceElement source;
    private final InfraTraceElement location;
    private final ServiceInfo serviceInfo;
    private final long timestamp;

    public CallerInfo(StackTraceElement source, InfraTraceElement location, ServiceInfo serviceInfo, long timestamp) {
        this.source = source;
        this.location = location;
        this.serviceInfo = serviceInfo;
        this.timestamp = timestamp;
    }

    public StackTraceElement getSource() {
        return source;
    }

    public InfraTraceElement getLocation() {
        return location;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
