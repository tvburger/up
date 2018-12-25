package net.tvburger.up.loggers.console;

import net.tvburger.up.runtime.logger.UpLogger;
import net.tvburger.up.runtime.spi.UpLoggerProvider;

import java.io.PrintStream;

public final class ConsoleLoggerProvider implements UpLoggerProvider {

    private static final PrintStream out = System.out;

    @Override
    public UpLogger getLogger(String loggerName) {
        return new ConsoleLogger(loggerName, out);
    }

}
