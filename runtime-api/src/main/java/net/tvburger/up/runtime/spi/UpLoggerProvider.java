package net.tvburger.up.runtime.spi;

import net.tvburger.up.runtime.logger.UpLogger;

public interface UpLoggerProvider {

    UpLogger getLogger(String loggerName);

}
