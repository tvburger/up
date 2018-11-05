package net.tvburger.up.impl;

import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.identity.Identity;

import java.util.UUID;

public class ServiceInfoImpl<T> implements ServiceInfo<T> {

    private final Class<T> serviceType;
    private final Identity identity;
    private final UUID serviceInstanceId;
    private final EnvironmentInfo environmentInfo;

    public ServiceInfoImpl(Class<T> serviceType, Identity identity, UUID serviceInstanceId, EnvironmentInfo environmentInfo) {
        this.serviceType = serviceType;
        this.identity = identity;
        this.serviceInstanceId = serviceInstanceId;
        this.environmentInfo = environmentInfo;
    }

    @Override
    public Class<T> getServiceType() {
        return serviceType;
    }

    @Override
    public Identity getServiceIdentity() {
        return identity;
    }

    @Override
    public UUID getServiceInstanceId() {
        return serviceInstanceId;
    }

    @Override
    public EnvironmentInfo getEnvironmentInfo() {
        return environmentInfo;
    }

    @Override
    public String toString() {
        return String.format("%s{%s:%s}@%s",
                serviceType == null ? "null" : serviceType.getSimpleName(),
                getServiceIdentity().getPrincipal().getName(),
                serviceInstanceId,
                environmentInfo.getName());
    }

}
