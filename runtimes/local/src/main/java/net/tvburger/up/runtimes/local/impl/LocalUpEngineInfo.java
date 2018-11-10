package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.impl.UpEngineInfoImpl;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;
import net.tvburger.up.util.LocalJavaImplementation;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class LocalUpEngineInfo extends UpEngineInfoImpl {

    public LocalUpEngineInfo(Identity identity) throws UnknownHostException {
        super(UUID.randomUUID(), InetAddress.getLocalHost(), -1, Identities.getSafeIdentification(identity), LocalJavaImplementation.get().getSpecification());
    }

}
