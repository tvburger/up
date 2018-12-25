package net.tvburger.up.logbindings;

import java.io.IOException;
import java.util.logging.LogManager;

public final class LogBindings {

    public static void init() {
        try {
            LogManager.getLogManager().readConfiguration(JavaUtilLoggingBinding.class.getClassLoader().getResourceAsStream("logging.properties"));
            UpLoggerStream.bind();
        } catch (SecurityException | IOException cause) {
            throw new ExceptionInInitializerError(cause);
        }
    }

    private LogBindings() {
    }

}
