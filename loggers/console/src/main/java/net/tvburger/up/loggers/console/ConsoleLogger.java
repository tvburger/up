package net.tvburger.up.loggers.console;

import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.runtime.context.CallerInfo;
import net.tvburger.up.runtime.context.Locality;
import net.tvburger.up.runtime.context.TransactionInfo;
import net.tvburger.up.runtime.logger.LogStatement;
import net.tvburger.up.runtime.logger.UpLogger;

import java.security.Principal;
import java.util.Date;

public final class ConsoleLogger implements UpLogger {

    private final String loggerName;

    public ConsoleLogger(String loggerName) {
        this.loggerName = loggerName;
    }

    @Override
    public void log(LogStatement logStatement) {
        ManagedEntity.Info entityInfo = logStatement.getEntityInfo();
        Locality locality = logStatement.getLocality();
        StackTraceElement source = logStatement.getSource();
        TransactionInfo transactionInfo = logStatement.getTransactionInfo();
        Principal requester = transactionInfo != null ? transactionInfo.getRequester() : null;
        CallerInfo callerInfo = logStatement.getCallerInfo();
        System.out.println(String.format(
                "%s [%s] <%s> %s {%s%s} (%s@%s@%s) [%s@%s] : %s",
                new Date(logStatement.getTimestamp()),
                logStatement.getLogLevel().name(),
                loggerName,
                entityInfo == null ? "?" : entityInfo.getClass().getSimpleName() + ":" + entityInfo.getIdentification().getPrincipal().getName(),
                transactionInfo == null ? "-" : transactionInfo.getId() + ", " + (requester == null ? "-" : requester.getName()) + ", " + transactionInfo.getRequestUri(),
                callerInfo == null ? "" : ", " + callerInfo.getOperationId()
                        + (callerInfo.getServiceInfo() != null ? ", " + callerInfo.getServiceInfo().getServiceType().getSimpleName() + ":" + callerInfo.getServiceInfo().getServiceInstanceId() : "")
                        + (callerInfo.getEndpointInfo() != null ? callerInfo.getEndpointInfo().getEndpointUri() : ""),
                locality == null ? "?" : locality.getRuntimeInfo().getClass().getSimpleName(),
                locality == null ? "?" : locality.getEngineInfo().getHost(),
                locality == null ? Thread.currentThread().getName() : locality.getThreadName(),
                source == null ? "?" : source.getFileName(),
                source == null ? "?" : source.getLineNumber(),
                logStatement.getMessage()));
    }

}
