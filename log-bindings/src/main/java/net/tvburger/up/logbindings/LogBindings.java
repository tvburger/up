package net.tvburger.up.logbindings;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.LogManager;

public final class LogBindings {

    private static final PrintStream stdout = System.out;
    private static final PrintStream stderr = System.err;

    public static PrintStream getStdout() {
        return stdout;
    }

    public static PrintStream getStderr() {
        return stderr;
    }

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
