package net.tvburger.up.logger;

import net.tvburger.up.ServiceInfo;
import net.tvburger.up.Up;
import net.tvburger.up.context.Locality;

import java.io.Serializable;

public class LogStatement implements Serializable {

    public static class Builder {

        private StackTraceElement source;
        private Locality locality;
        private ServiceInfo<?> serviceInfo;
        private Long timestamp;
        private LogLevel logLevel;
        private String message;

        public Builder withSource(StackTraceElement source) {
            this.source = source;
            return this;
        }

        public Builder withLocality(Locality locality) {
            this.locality = locality;
            return this;
        }

        public Builder withServiceInfo(ServiceInfo<?> serviceInfo) {
            this.serviceInfo = serviceInfo;
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withLogLevel(LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public LogStatement build() {
            return new LogStatement(
                    source == null ? getStackTraceElement() : source,
                    locality == null ? Up.getContext() != null ? Up.getContext().getLocality() : null : locality,
                    serviceInfo == null ? Up.getContext() != null ? Up.getContext().getServiceInfo() : null : serviceInfo,
                    timestamp == null ? System.currentTimeMillis() : timestamp,
                    logLevel == null ? LogLevel.INFO : logLevel,
                    message == null ? "<no message>" : message);
        }

        private StackTraceElement getStackTraceElement() {
            return Thread.currentThread().getStackTrace()[3];
        }

    }

    private final StackTraceElement source;
    private final Locality locality;
    private final ServiceInfo<?> serviceInfo;
    private final long timestamp;
    private final LogLevel logLevel;
    private final String message;

    public LogStatement(StackTraceElement source, Locality locality, ServiceInfo<?> serviceInfo, long timestamp, LogLevel logLevel, String message) {
        this.source = source;
        this.locality = locality;
        this.serviceInfo = serviceInfo;
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.message = message;
    }

    public StackTraceElement getSource() {
        return source;
    }

    public Locality getLocality() {
        return locality;
    }

    public ServiceInfo<?> getServiceInfo() {
        return serviceInfo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getMessage() {
        return message;
    }
}
