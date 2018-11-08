package net.tvburger.up.impl;

import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.security.Identification;

import java.util.Objects;
import java.util.UUID;

public class ServiceInfoImpl<T> implements ServiceInfo<T> {

    public static final class Factory {

        public static <T> ServiceInfoImpl<T> create(Class<T> serviceType, Identification identification, UUID serviceInstanceId, EnvironmentInfo environmentInfo) {
            Objects.requireNonNull(serviceType);
            return create(
                    SpecificationImpl.Factory.create(serviceType.getName(), "unversioned"),
                    serviceType, identification, serviceInstanceId, environmentInfo);
        }

        public static <T> ServiceInfoImpl<T> create(Specification specification, Class<T> serviceType, Identification identification, UUID serviceInstanceId, EnvironmentInfo environmentInfo) {
            Objects.requireNonNull(specification);
            Objects.requireNonNull(serviceType);
            Objects.requireNonNull(identification);
            Objects.requireNonNull(serviceInstanceId);
            Objects.requireNonNull(environmentInfo);
            return new ServiceInfoImpl<>(specification, serviceType, identification, serviceInstanceId, environmentInfo);
        }

        private Factory() {
        }

    }

    private final Specification specification;
    private final Class<T> serviceType;
    private final Identification identification;
    private final UUID serviceInstanceId;
    private final EnvironmentInfo environmentInfo;

    protected ServiceInfoImpl(Specification specification, Class<T> serviceType, Identification identification, UUID serviceInstanceId, EnvironmentInfo environmentInfo) {
        this.specification = specification;
        this.serviceType = serviceType;
        this.identification = identification;
        this.serviceInstanceId = serviceInstanceId;
        this.environmentInfo = environmentInfo;
    }

    @Override
    public Class<T> getServiceType() {
        return serviceType;
    }

    @Override
    public Identification getIdentification() {
        return identification;
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
    public String getSpecificationName() {
        return specification.getSpecificationName();
    }

    @Override
    public String getSpecificationVersion() {
        return specification.getSpecificationVersion();
    }

    @Override
    public String toString() {
        return String.format("ServiceInfo{%s, %s, %s, %s, %s}",
                specification,
                serviceType,
                identification,
                serviceInstanceId,
                environmentInfo);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof ServiceInfo
                        && Objects.equals(getServiceType(), ((ServiceInfo) object).getServiceType())
                        && Objects.equals(getIdentification(), ((ServiceInfo) object).getIdentification())
                        && Objects.equals(getServiceInstanceId(), ((ServiceInfo) object).getServiceInstanceId())
                        && Objects.equals(getEnvironmentInfo(), ((ServiceInfo) object).getEnvironmentInfo())
                        && Objects.equals(getSpecificationName(), ((ServiceInfo) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((ServiceInfo) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return 7 + Objects.hashCode(specification) * 31
                + Objects.hashCode(serviceType) * 41
                + Objects.hashCode(identification) * 13
                + Objects.hashCode(serviceInstanceId) * 11
                + Objects.hashCode(environmentInfo) * 3;
    }

}
