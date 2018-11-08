package net.tvburger.up.spi;

import net.tvburger.up.logger.UpLogger;

public interface UpLoggerProvider {

    UpLogger getLogger(String loggerName);

}
