package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public final class StaticLoggerBinder implements LoggerFactoryBinder {

    public static final String REQUESTED_API_VERSION = "1.7.25";

    private static final StaticLoggerBinder singleton = new StaticLoggerBinder();
    private static final ILoggerFactory loggerFactory = new Slf4jLogger.Factory();
    private static final String loggerFactoryClassStr = Slf4jLogger.class.getName();

    public static final StaticLoggerBinder getSingleton() {
        return singleton;
    }

    private StaticLoggerBinder() {
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return loggerFactoryClassStr;
    }

}
