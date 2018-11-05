package net.tvburger.up.local;

import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.deploy.UpLanguageInterpreter;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.identity.Identity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class LocalUpEngine implements UpEngine {

    public static class Factory {

        public static LocalUpEngine create() throws UnknownHostException {
            return new LocalUpEngine(InetAddress.getLocalHost());
        }

        private Factory() {
        }

    }

    private final UUID uuid = UUID.randomUUID();
    private final InetAddress host;

    private LocalUpEngine(InetAddress host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return -1;
    }

    @Override
    public InetAddress getHost() {
        return host;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public UpRuntime getRuntime() {
        return LocalUpInstance.get().getRuntime();
    }

    @Override
    public Set<UpLanguageInterpreter> getLanguageInterpreters() {
        return Collections.singleton(LocalUpInstance.get().getInterpreter());
    }

    @Override
    public Identity getIdentity() {
        return LocalUpInstance.get().getRuntime().getIdentity();
    }

}
