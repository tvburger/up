package net.tvburger.up.logbindings;

import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.runtime.logger.LogLevel;
import net.tvburger.up.runtime.logger.LogStatement;
import net.tvburger.up.runtime.logger.UpLogger;
import net.tvburger.up.runtime.util.UpLoggers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public final class UpLoggerStream extends OutputStream {

    public static void bind() {
        System.setErr(new PrintStream(new UpLoggerStream(System.err, UpLoggers.getLogger("stderr"), LogLevel.ERROR)));
        System.setOut(new PrintStream(new UpLoggerStream(System.out, UpLoggers.getLogger("stdout"), LogLevel.INFO)));
    }

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final UpLogger logger;
    private final LogLevel logLevel;
    private final PrintStream out;

    private UpContext currentContext;
    private UpContext previousContext;
    private long startTimestamp = -1;

    public UpLoggerStream(PrintStream out, UpLogger logger, LogLevel logLevel) {
        this.logger = logger;
        this.logLevel = logLevel;
        this.out = out;
    }

    private boolean contextChanged() {
        previousContext = currentContext;
        currentContext = UpContext.getContext();
        return !UpContext.equals(previousContext, currentContext);
    }

    private void handleContextChange() throws IOException {
        if (contextChanged() && outputStream.size() > 0) {
            doOutput(previousContext);
        }
    }

    private void doOutput(UpContext context) throws IOException {
        if (context != null) {
            logger.log(new LogStatement.Builder()
                    .withContext(context)
                    .withTimestamp(startTimestamp)
                    .withSource(getStackTraceElement())
                    .withLogLevel(logLevel)
                    .withMessage(getOutputString())
                    .build());
        } else {
            out.write(outputStream.toByteArray());
            out.flush();
        }
        outputStream.reset();
        startTimestamp = -1;
    }

    private StackTraceElement getStackTraceElement() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (!element.getClassName().startsWith("java") && !element.getClassName().startsWith("sun.") && !element.getClassName().equals(UpLoggerStream.class.getName())) {
                return element;
            }
        }
        return null;
    }

    private String getOutputString() {
        String output = outputStream.toString();
        return output.endsWith("\n") ? output.substring(0, output.length() - 1) : output;
    }

    @Override
    public synchronized void write(int b) throws IOException {
        handleContextChange();
        if (startTimestamp == -1) {
            startTimestamp = System.currentTimeMillis();
        }
        outputStream.write(b);
        if (b == '\n') {
            doOutput(currentContext);
        }
    }

    @Override
    public synchronized void write(byte[] b) throws IOException {
        handleContextChange();
        if (startTimestamp == -1) {
            startTimestamp = System.currentTimeMillis();
        }
        outputStream.write(b);
        if (currentContext == null || b[b.length - 1] == '\n') {
            doOutput(currentContext);
        }
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        handleContextChange();
        if (startTimestamp == -1) {
            startTimestamp = System.currentTimeMillis();
        }
        outputStream.write(b, off, len);
        if (currentContext == null || b[off + len] == '\n') {
            doOutput(currentContext);
        }
    }

    @Override
    public synchronized void flush() throws IOException {
        if (outputStream.size() > 0) {
            doOutput(currentContext);
        }
    }

}
