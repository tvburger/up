package net.tvburger.up.runtime.logger;

import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.runtime.context.CallerInfo;
import net.tvburger.up.runtime.context.Locality;
import net.tvburger.up.runtime.context.TransactionInfo;
import net.tvburger.up.runtime.context.UpContext;

import java.io.Serializable;
import java.util.UUID;

public class LogStatement implements Serializable {

    public static class Builder {

        private UUID operationId;
        private TransactionInfo transactionInfo;
        private CallerInfo callerInfo;
        private StackTraceElement source;
        private Locality locality;
        private ManagedEntity.Info entityInfo;
        private Long timestamp;
        private LogLevel logLevel;
        private String message;

        public Builder withOperationId(UUID operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder withTransactionInfo(TransactionInfo transactionInfo) {
            this.transactionInfo = transactionInfo;
            return this;
        }

        public Builder withCallerInfo(CallerInfo callerInfo) {
            this.callerInfo = callerInfo;
            return this;
        }

        public Builder withSource(StackTraceElement source) {
            this.source = source;
            return this;
        }

        public Builder withLocality(Locality locality) {
            this.locality = locality;
            return this;
        }

        public Builder withEntityInfo(ManagedEntity.Info entityInfo) {
            this.entityInfo = entityInfo;
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
            UpContext context;
            if (operationId == null || transactionInfo == null || callerInfo == null || entityInfo == null || locality == null) {
                context = UpContext.getContext();
            } else {
                context = null;
            }
            return new LogStatement(
                    operationId == null ? context.getOperationId() : null,
                    transactionInfo == null ? context.getTransactionInfo() : transactionInfo,
                    callerInfo == null ? context.getCallerInfo() : callerInfo,
                    source == null ? getStackTraceElement() : source,
                    locality == null ? context.getLocality() : locality,
                    entityInfo == null ? getEntityInfo(context) : entityInfo,
                    timestamp == null ? System.currentTimeMillis() : timestamp,
                    logLevel == null ? LogLevel.INFO : logLevel,
                    message == null ? "<no message>" : message);
        }

        private ManagedEntity.Info getEntityInfo(UpContext context) {
            ManagedEntity.Info info = context.getService() != null ? context.getService().getInfo() :
                    context.getEndpoint() != null ? context.getEndpoint().getInfo() :
                            context.getEngine() != null ? context.getEngine().getInfo() :
                                    context.getRuntime() != null ? context.getRuntime().getInfo() :
                                            null;
            if (info == null) {
                throw new NullPointerException();
            }
            return info;
        }

        private StackTraceElement getStackTraceElement() {
            return Thread.currentThread().getStackTrace()[3];
        }

    }

    private final UUID operationId;
    private final TransactionInfo transactionInfo;
    private final CallerInfo callerInfo;
    private final StackTraceElement source;
    private final Locality locality;
    private final ManagedEntity.Info entityInfo;
    private final long timestamp;
    private final LogLevel logLevel;
    private final String message;

    public LogStatement(UUID operationId, TransactionInfo transactionInfo, CallerInfo callerInfo, StackTraceElement source, Locality locality, ManagedEntity.Info entityInfo, long timestamp, LogLevel logLevel, String message) {
        this.operationId = operationId;
        this.transactionInfo = transactionInfo;
        this.callerInfo = callerInfo;
        this.source = source;
        this.locality = locality;
        this.entityInfo = entityInfo;
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.message = message;
    }

    public UUID getOperationId() {
        return operationId;
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public CallerInfo getCallerInfo() {
        return callerInfo;
    }

    public StackTraceElement getSource() {
        return source;
    }

    public Locality getLocality() {
        return locality;
    }

    public ManagedEntity.Info getEntityInfo() {
        return entityInfo;
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
