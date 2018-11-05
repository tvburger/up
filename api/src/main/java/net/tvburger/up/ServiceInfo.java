package net.tvburger.up;

import net.tvburger.up.identity.Identity;

import java.io.Serializable;
import java.util.UUID;

public interface ServiceInfo<T> extends Serializable {

    Class<T> getServiceType();

    Identity getServiceIdentity();

    UUID getServiceInstanceId();

    EnvironmentInfo getEnvironmentInfo();

}
