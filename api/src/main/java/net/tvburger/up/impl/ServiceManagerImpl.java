package net.tvburger.up.impl;

import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.ServiceManager;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.security.Identity;

import java.util.Objects;
import java.util.UUID;

public class ServiceManagerImpl<T> extends LifecycleManagerImpl implements ServiceManager<T> {

    public static final class Factory {

        public static <T> ServiceManagerImpl<T> create(Implementation implementation, Class<T> serviceType, Identity identity, UUID serviceInstanceId, EnvironmentInfo environmentInfo) {
            Objects.requireNonNull(implementation);
            return create(
                    implementation,
                    ServiceInfoImpl.Factory.create(implementation.getSpecification(), serviceType, identity, serviceInstanceId, environmentInfo));
        }

        public static <T> ServiceManagerImpl<T> create(Implementation implementation, ServiceInfo<T> serviceInfo) {
            Objects.requireNonNull(implementation);
            Objects.requireNonNull(serviceInfo);
            Specification specification = implementation.getSpecification();
            if (!Objects.equals(specification.getSpecificationName(), serviceInfo.getSpecificationName())
                    || !Objects.equals(specification.getSpecificationVersion(), serviceInfo.getSpecificationVersion())) {
                throw new IllegalArgumentException();
            }
            return new ServiceManagerImpl<>(implementation, serviceInfo);
        }

    }

    private final Implementation implementation;
    private final ServiceInfo<T> serviceInfo;
    private volatile boolean logged;

    protected ServiceManagerImpl(Implementation implementation, ServiceInfo<T> serviceInfo) {
        this.implementation = implementation;
        this.serviceInfo = serviceInfo;
    }

    @Override
    public ServiceInfo<T> getInfo() {
        return serviceInfo;
    }

    @Override
    public boolean isLogged() {
        return logged;
    }

    @Override
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public synchronized String getImplementationName() {
        return implementation.getImplementationName();
    }

    @Override
    public synchronized String getImplementationVersion() {
        return implementation.getImplementationVersion();
    }

    @Override
    public Specification getSpecification() {
        return implementation.getSpecification();
    }

}
