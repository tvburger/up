package net.tvburger.up.loggers.console;

import net.tvburger.up.logger.UpLogger;
import net.tvburger.up.spi.UpLoggerProvider;

public final class ConsoleLoggerProvider implements UpLoggerProvider {

    @Override
    public UpLogger getLogger(String loggerName) {
        return new ConsoleLogger(loggerName);
    }

}
