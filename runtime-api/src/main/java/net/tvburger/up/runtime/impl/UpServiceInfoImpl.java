package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.SpecificationImpl;
import net.tvburger.up.security.Identification;

import java.util.Objects;
import java.util.UUID;

public class UpServiceInfoImpl<T> implements UpService.Info<T> {

    public static final class Factory {

        public static <T> UpServiceInfoImpl<T> create(Class<T> serviceType, Identification identification, UUID serviceInstanceId, UpEnvironment.Info environmentInfo) {
            Objects.requireNonNull(serviceType);
            return create(
                    SpecificationImpl.Factory.create(serviceType.getName(), "unversioned"),
                    serviceType, identification, serviceInstanceId, environmentInfo);
        }

        public static <T> UpServiceInfoImpl<T> create(Specification specification, Class<T> serviceType, Identification identification, UUID serviceInstanceId, UpEnvironment.Info environmentInfo) {
            Objects.requireNonNull(specification);
            Objects.requireNonNull(serviceType);
            Objects.requireNonNull(identification);
            Objects.requireNonNull(serviceInstanceId);
            Objects.requireNonNull(environmentInfo);
            return new UpServiceInfoImpl<>(specification, serviceType, identification, serviceInstanceId, environmentInfo);
        }

        private Factory() {
        }

    }

    private final Specification specification;
    private final Class<T> serviceType;
    private final Identification identification;
    private final UUID serviceInstanceId;
    private final UpEnvironment.Info environmentInfo;

    protected UpServiceInfoImpl(Specification specification, Class<T> serviceType, Identification identification, UUID serviceInstanceId, UpEnvironment.Info environmentInfo) {
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
    public UpEnvironment.Info getEnvironmentInfo() {
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
        return String.format("UpService.Info{%s, %s, %s, %s, %s}",
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
                (object instanceof UpService.Info
                        && Objects.equals(getServiceType(), ((UpService.Info) object).getServiceType())
                        && Objects.equals(getIdentification(), ((UpService.Info) object).getIdentification())
                        && Objects.equals(getServiceInstanceId(), ((UpService.Info) object).getServiceInstanceId())
                        && Objects.equals(getEnvironmentInfo(), ((UpService.Info) object).getEnvironmentInfo())
                        && Objects.equals(getSpecificationName(), ((UpService.Info) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((UpService.Info) object).getSpecificationVersion()));
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
