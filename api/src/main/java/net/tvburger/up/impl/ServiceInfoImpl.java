package net.tvburger.up.impl;

import net.tvburger.up.ServiceInfo;

import java.security.PublicKey;
import java.util.UUID;

public class ServiceInfoImpl<T> implements ServiceInfo<T> {

    private final String environment;
    private final Class<T> serviceType;
    private final PublicKey serviceIdentity;
    private final UUID serviceInstanceId;

    public ServiceInfoImpl(String environment, Class<T> serviceType, PublicKey serviceIdentity, UUID serviceInstanceId) {
        this.environment = environment;
        this.serviceType = serviceType;
        this.serviceIdentity = serviceIdentity;
        this.serviceInstanceId = serviceInstanceId;
    }

    @Override
    public String getEnvironment() {
        return environment;
    }

    @Override
    public Class<T> getServiceType() {
        return serviceType;
    }

    @Override
    public PublicKey getServiceIdentity() {
        return serviceIdentity;
    }

    @Override
    public UUID getServiceInstanceId() {
        return serviceInstanceId;
    }
}
