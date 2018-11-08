package net.tvburger.up.logger.impl;

import net.tvburger.up.ServiceInfo;
import net.tvburger.up.context.Locality;
import net.tvburger.up.logger.LogStatement;
import net.tvburger.up.logger.UpLogger;

import java.util.Date;

public final class ConsoleLogger implements UpLogger {

    private final String loggerName;

    public ConsoleLogger(String loggerName) {
        this.loggerName = loggerName;
    }

    @Override
    public void log(LogStatement logStatement) {
        ServiceInfo<?> serviceInfo = logStatement.getServiceInfo();
        Locality locality = logStatement.getLocality();
        StackTraceElement source = logStatement.getSource();
        System.out.println(String.format(
                "%s [%s] <%s> %s(%s@%s%s) [%s@%s] : %s",
                new Date(logStatement.getTimestamp()),
                logStatement.getLogLevel().name(),
                loggerName,
                serviceInfo == null ? "" :
                        String.format("%s{%s@%s} ",
                                serviceInfo.getServiceType().getSimpleName(),
                                serviceInfo.getIdentification().getPrincipal().getName(),
                                serviceInfo.getServiceInstanceId()),
                locality == null ? "?" : locality.getEngineInfo().getHost(),
                locality == null ? "?" : Thread.currentThread().getName(),
                serviceInfo == null ? "" : serviceInfo.getEnvironmentInfo() == null ? "" : ":" + logStatement.getServiceInfo().getEnvironmentInfo().getName(),
                source == null ? "?" : source.getFileName(),
                source == null ? "?" : source.getLineNumber(),
                logStatement.getMessage()));
    }

}
