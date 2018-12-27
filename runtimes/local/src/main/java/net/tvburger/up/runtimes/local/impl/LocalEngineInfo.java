package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.logbindings.LogBindings;
import net.tvburger.up.runtime.impl.UpEngineInfoImpl;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;
import net.tvburger.up.util.LocalJavaImplementation;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class LocalEngineInfo extends UpEngineInfoImpl {

    static {
        LogBindings.init();
    }

    public LocalEngineInfo(Identity identity) throws UnknownHostException {
        super(InetAddress.getLocalHost(), -1, Identities.getSafeIdentification(identity), LocalJavaImplementation.get().getSpecification());
    }

}
