package net.tvburger.up.logger.impl;

import net.tvburger.up.Up;
import net.tvburger.up.logger.LogLevel;
import net.tvburger.up.logger.LogStatement;
import net.tvburger.up.logger.UpLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class LogHandler extends StreamHandler {

    private final Map<String, UpLogger> loggers = new HashMap<>();

    @Override
    public synchronized void publish(LogRecord record) {
        UpLogger logger = loggers.computeIfAbsent(record.getLoggerName(), Up::getLogger);
        logger.log(recordToLogStatement(record));
    }

    private LogStatement recordToLogStatement(LogRecord record) {
        return new LogStatement.Builder()
                .withLogLevel(mapLogLevel(record.getLevel()))
                .withMessage(record.getMessage())
                .withTimestamp(record.getMillis())
                .build();
    }

    private LogLevel mapLogLevel(Level level) {
        switch (level.getName()) {
            case "SEVERE":
                return LogLevel.ERROR;
            case "WARNING":
                return LogLevel.WARN;
            case "INFO":
                return LogLevel.INFO;
            case "CONFIG":
            case "FINE":
                return LogLevel.DEBUG;
            default:
                return LogLevel.TRACE;
        }
    }

}
