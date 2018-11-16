package net.tvburger.up.runtime.util;

import net.tvburger.up.runtime.logger.UpLogger;
import net.tvburger.up.runtime.spi.UpLoggerProvider;

import java.util.ServiceLoader;

public final class UpLoggers {

    private static final UpLoggerProvider loggerProvider = ServiceLoader.load(UpLoggerProvider.class).iterator().next();

    public static UpLogger getLogger(String loggerName) {
        return loggerProvider.getLogger(loggerName);
    }

    private UpLoggers() {
    }

}
