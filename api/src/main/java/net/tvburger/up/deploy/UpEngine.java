package net.tvburger.up.deploy;

import net.tvburger.up.identity.Entity;

import java.net.InetAddress;
import java.util.Set;
import java.util.UUID;

public interface UpEngine extends Entity {

    int getPort();

    InetAddress getHost();

    UUID getUUID();

    UpRuntime getRuntime();

    Set<UpLanguageInterpreter> getLanguageInterpreters();

}
