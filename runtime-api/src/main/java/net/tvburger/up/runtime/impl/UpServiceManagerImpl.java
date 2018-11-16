package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.security.Identity;

import java.util.Objects;
import java.util.UUID;

public class UpServiceManagerImpl<T> extends LifecycleManagerImpl implements UpService.Manager<T> {

    public static final class Factory {

        public static <T> UpServiceManagerImpl<T> create(Implementation implementation, Class<T> serviceType, Identity identity, UUID serviceInstanceId, UpEnvironment.Info environmentInfo) {
            Objects.requireNonNull(implementation);
            return create(
                    implementation,
                    UpServiceInfoImpl.Factory.create(implementation.getSpecification(), serviceType, identity, serviceInstanceId, environmentInfo));
        }

        public static <T> UpServiceManagerImpl<T> create(Implementation implementation, UpService.Info<T> serviceInfo) {
            Objects.requireNonNull(implementation);
            Objects.requireNonNull(serviceInfo);
            Specification specification = implementation.getSpecification();
            if (!Objects.equals(specification.getSpecificationName(), serviceInfo.getSpecificationName())
                    || !Objects.equals(specification.getSpecificationVersion(), serviceInfo.getSpecificationVersion())) {
                throw new IllegalArgumentException();
            }
            return new UpServiceManagerImpl<>(implementation, serviceInfo);
        }

    }

    private final Implementation implementation;
    private final UpService.Info<T> serviceInfo;
    private volatile boolean logged = true;

    protected UpServiceManagerImpl(Implementation implementation, UpService.Info<T> serviceInfo) {
        this.implementation = implementation;
        this.serviceInfo = serviceInfo;
    }

    @Override
    public UpService.Info<T> getInfo() {
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
