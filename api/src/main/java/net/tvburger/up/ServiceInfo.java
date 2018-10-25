package net.tvburger.up;

import java.security.PublicKey;
import java.util.UUID;

public interface ServiceInfo<T> {

    String getEnvironment();

    Class<T> getServiceType();

    PublicKey getServiceIdentity();

    UUID getServiceInstanceId();

}
