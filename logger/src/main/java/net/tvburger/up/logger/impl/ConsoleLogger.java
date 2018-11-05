package net.tvburger.up.logger.impl;

import net.tvburger.up.logger.LogStatement;
import net.tvburger.up.logger.Logger;

import java.util.Date;

public class ConsoleLogger implements Logger {

    @Override
    public void log(LogStatement logStatement) {
        System.out.println(String.format(
                "[%s] %s %s{%s@%s} (%s@%s%s) [%s@%s] %s",
                logStatement.getLogLevel().name(),
                new Date(logStatement.getTimestamp()),
                logStatement.getServiceInfo().getServiceType().getSimpleName(),
                logStatement.getServiceInfo().getServiceIdentity().getPrincipal().getName(),
                logStatement.getServiceInfo().getServiceInstanceId(),
                logStatement.getLocation().getHostName(),
                logStatement.getLocation().getThreadName(),
                logStatement.getServiceInfo().getEnvironmentInfo() == null ? "" : ":" + logStatement.getServiceInfo().getEnvironmentInfo().getName(),
                logStatement.getSource().getFileName(),
                logStatement.getSource().getLineNumber(),
                logStatement.getMessage()));
    }

}
