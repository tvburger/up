package net.tvburger.up.logger;

import net.tvburger.up.ServiceInfo;

import java.io.Serializable;

public class LogStatement implements Serializable {

    public static class Builder {

        private StackTraceElement source;
        private InfraTraceElement location;
        private ServiceInfo serviceInfo;
        private Long timestamp;
        private LogLevel logLevel;
        private String message;
        private StackTraceElement stackTraceElement;

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

        public Builder withLogLevel(LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public boolean isValid() {
            return serviceInfo != null;
        }

        public LogStatement build() {
            if (!isValid()) {
                throw new IllegalStateException();
            }
            return new LogStatement(
                    source == null ? getStackTraceElement() : source,
                    location == null ? createLocation() : location,
                    serviceInfo,
                    timestamp == null ? System.currentTimeMillis() : timestamp,
                    logLevel == null ? LogLevel.INFO : logLevel,
                    message == null ? "<no message>" : message);
        }

        private InfraTraceElement createLocation() {
            return new InfraTraceElement("localhost", Thread.currentThread().getName());
        }

        private StackTraceElement getStackTraceElement() {
            return Thread.currentThread().getStackTrace()[3];
        }

    }

    private final StackTraceElement source;
    private final InfraTraceElement location;
    private final ServiceInfo serviceInfo;
    private final long timestamp;
    private final LogLevel logLevel;
    private final String message;

    public LogStatement(StackTraceElement source, InfraTraceElement location, ServiceInfo serviceInfo, long timestamp, LogLevel logLevel, String message) {
        this.source = source;
        this.location = location;
        this.serviceInfo = serviceInfo;
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.message = message;
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

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getMessage() {
        return message;
    }
}
