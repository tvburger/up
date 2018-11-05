package org.slf4j.impl;

import net.tvburger.up.logger.LogLevel;
import net.tvburger.up.logger.LogStatement;
import net.tvburger.up.logger.Logger;
import net.tvburger.up.logger.impl.ConsoleLogger;
import org.slf4j.ILoggerFactory;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class UpLoggerAdapter extends MarkerIgnoringBase implements org.slf4j.Logger {

    public static class Factory implements ILoggerFactory {

        private final UpLoggerAdapter logger = new UpLoggerAdapter(new ConsoleLogger());

        @Override
        public org.slf4j.Logger getLogger(String name) {
            return logger;
        }

    }

    private final Logger logger;

    public UpLoggerAdapter(Logger logger) {
        this.logger = logger;
    }

    private void log(LogLevel level, String message, Throwable t) {
        LogStatement.Builder builder = new LogStatement.Builder()
                .withLogLevel(level)
                .withSource(getStackTraceElement());
        logger.log(builder.withMessage(message).build());
        if (t != null) {
            try (StringWriter stringWriter = new StringWriter(); PrintWriter printWriter = new PrintWriter(stringWriter)) {
                t.printStackTrace(printWriter);
                logger.log(builder.withMessage(stringWriter.getBuffer().toString()).build());
            } catch (IOException cause) {
                throw new IllegalStateException(cause);
            }
        }
    }

    private StackTraceElement getStackTraceElement() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (!element.getClassName().equals(UpLoggerAdapter.class.getName())
                    && !element.getClassName().equals(Thread.class.getName())) {
                return element;
            }
        }
        throw new IllegalStateException();
    }

    private void formatAndLog(LogLevel level, String format, Object arg1, Object arg2) {
        if (this.isLevelEnabled(level)) {
            FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
            this.log(level, tp.getMessage(), tp.getThrowable());
        }
    }

    private void formatAndLog(LogLevel level, String format, Object... arguments) {
        if (this.isLevelEnabled(level)) {
            FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
            this.log(level, tp.getMessage(), tp.getThrowable());
        }
    }

    protected boolean isLevelEnabled(LogLevel level) {
        // TODO: implement
        return true;
    }

    public boolean isTraceEnabled() {
        return this.isLevelEnabled(LogLevel.TRACE);
    }

    public void trace(String msg) {
        this.log(LogLevel.TRACE, msg, (Throwable) null);
    }

    public void trace(String format, Object param1) {
        this.formatAndLog(LogLevel.TRACE, format, param1, (Object) null);
    }

    public void trace(String format, Object param1, Object param2) {
        this.formatAndLog(LogLevel.TRACE, format, param1, param2);
    }

    public void trace(String format, Object... argArray) {
        this.formatAndLog(LogLevel.TRACE, format, argArray);
    }

    public void trace(String msg, Throwable t) {
        this.log(LogLevel.TRACE, msg, t);
    }

    public boolean isDebugEnabled() {
        return this.isLevelEnabled(LogLevel.DEBUG);
    }

    public void debug(String msg) {
        this.log(LogLevel.DEBUG, msg, (Throwable) null);
    }

    public void debug(String format, Object param1) {
        this.formatAndLog(LogLevel.DEBUG, format, param1, (Object) null);
    }

    public void debug(String format, Object param1, Object param2) {
        this.formatAndLog(LogLevel.DEBUG, format, param1, param2);
    }

    public void debug(String format, Object... argArray) {
        this.formatAndLog(LogLevel.DEBUG, format, argArray);
    }

    public void debug(String msg, Throwable t) {
        this.log(LogLevel.DEBUG, msg, t);
    }

    public boolean isInfoEnabled() {
        return this.isLevelEnabled(LogLevel.INFO);
    }

    public void info(String msg) {
        this.log(LogLevel.INFO, msg, (Throwable) null);
    }

    public void info(String format, Object arg) {
        this.formatAndLog(LogLevel.INFO, format, arg, (Object) null);
    }

    public void info(String format, Object arg1, Object arg2) {
        this.formatAndLog(LogLevel.INFO, format, arg1, arg2);
    }

    public void info(String format, Object... argArray) {
        this.formatAndLog(LogLevel.INFO, format, argArray);
    }

    public void info(String msg, Throwable t) {
        this.log(LogLevel.INFO, msg, t);
    }

    public boolean isWarnEnabled() {
        return this.isLevelEnabled(LogLevel.WARN);
    }

    public void warn(String msg) {
        this.log(LogLevel.WARN, msg, (Throwable) null);
    }

    public void warn(String format, Object arg) {
        this.formatAndLog(LogLevel.WARN, format, arg, (Object) null);
    }

    public void warn(String format, Object arg1, Object arg2) {
        this.formatAndLog(LogLevel.WARN, format, arg1, arg2);
    }

    public void warn(String format, Object... argArray) {
        this.formatAndLog(LogLevel.WARN, format, argArray);
    }

    public void warn(String msg, Throwable t) {
        this.log(LogLevel.WARN, msg, t);
    }

    public boolean isErrorEnabled() {
        return this.isLevelEnabled(LogLevel.ERROR);
    }

    public void error(String msg) {
        this.log(LogLevel.ERROR, msg, (Throwable) null);
    }

    public void error(String format, Object arg) {
        this.formatAndLog(LogLevel.ERROR, format, arg, (Object) null);
    }

    public void error(String format, Object arg1, Object arg2) {
        this.formatAndLog(LogLevel.ERROR, format, arg1, arg2);
    }

    public void error(String format, Object... argArray) {
        this.formatAndLog(LogLevel.ERROR, format, argArray);
    }

    public void error(String msg, Throwable t) {
        this.log(LogLevel.ERROR, msg, t);
    }

    public void log(LoggingEvent event) {
        LogLevel level = LogLevel.valueOf(event.getLevel().name());
        if (this.isLevelEnabled(level)) {
            FormattingTuple tp = MessageFormatter.arrayFormat(event.getMessage(), event.getArgumentArray(), event.getThrowable());
            this.log(level, tp.getMessage(), event.getThrowable());
        }
    }

}
