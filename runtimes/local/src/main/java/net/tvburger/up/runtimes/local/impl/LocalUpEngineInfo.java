package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.impl.UpEngineInfoImpl;
import net.tvburger.up.logger.impl.LogHandler;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;
import net.tvburger.up.util.LocalJavaImplementation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.LogManager;

public final class LocalUpEngineInfo extends UpEngineInfoImpl {

    static {
        try {
            LogManager.getLogManager().readConfiguration(LogHandler.class.getClassLoader().getResourceAsStream("logging.properties"));
        } catch (SecurityException | IOException cause) {
            throw new ExceptionInInitializerError(cause);
        }
    }

    public LocalUpEngineInfo(Identity identity) throws UnknownHostException {
        super(UUID.randomUUID(), InetAddress.getLocalHost(), -1, Identities.getSafeIdentification(identity), LocalJavaImplementation.get().getSpecification());
    }

}
