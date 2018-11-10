package net.tvburger.up.runtime;

import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.behaviors.Specification;

import java.net.InetAddress;
import java.util.UUID;

public interface UpEngineInfo extends Specification, ManagedEntity.Info {

    UUID getUuid();

    InetAddress getHost();

    int getPort();

}
