package net.tvburger.up.logger.impl;

import net.tvburger.up.logger.LogStatement;
import net.tvburger.up.logger.Logger;

public class ConsoleLogger implements Logger {

    @Override
    public void log(LogStatement logStatement) {
        System.out.println(String.format(
                "[%s] %s %s{%s} (%s@%s:%s) [%s@%s] %s",
                logStatement.getLogLevel().name(),
                logStatement.getTimestamp(),
                logStatement.getServiceInfo().getServiceType().getSimpleName(),
                logStatement.getServiceInfo().getServiceInstanceId(),
                logStatement.getLocation().getHostName(),
                logStatement.getLocation().getThreadName(),
                logStatement.getServiceInfo().getEnvironment(),
                logStatement.getSource().getFileName(),
                logStatement.getSource().getLineNumber(),
                logStatement.getMessage()));
    }

}
