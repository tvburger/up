package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.SpecificationImpl;
import net.tvburger.up.security.Identification;

import java.util.Objects;
import java.util.UUID;

public class UpServiceInfoImpl<T> implements UpService.Info<T> {

    public static final class Factory {

        public static <T> UpServiceInfoImpl<T> create(Class<T> serviceType, Identification identification, UUID serviceInstanceId, UpApplication.Info applicationInfo) {
            Objects.requireNonNull(serviceType);
            return create(
                    SpecificationImpl.Factory.create(serviceType.getCanonicalName(), "unversioned"),
                    serviceType, identification, serviceInstanceId, applicationInfo);
        }

        public static <T> UpServiceInfoImpl<T> create(Specification specification, Class<T> serviceType, Identification identification, UUID serviceInstanceId, UpApplication.Info applicationInfo) {
            Objects.requireNonNull(specification);
            Objects.requireNonNull(serviceType);
            Objects.requireNonNull(identification);
            Objects.requireNonNull(serviceInstanceId);
            Objects.requireNonNull(applicationInfo);
            return new UpServiceInfoImpl<>(specification, serviceType, identification, serviceInstanceId, applicationInfo);
        }

        private Factory() {
        }

    }

    private final Specification specification;
    private final Class<T> serviceType;
    private final Identification identification;
    private final UUID serviceInstanceId;
    private final UpApplication.Info applicationInfo;

    protected UpServiceInfoImpl(Specification specification, Class<T> serviceType, Identification identification, UUID serviceInstanceId, UpApplication.Info applicationInfo) {
        this.specification = specification;
        this.serviceType = serviceType;
        this.identification = identification;
        this.serviceInstanceId = serviceInstanceId;
        this.applicationInfo = applicationInfo;
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
    public UpApplication.Info getApplicationInfo() {
        return applicationInfo;
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
                serviceType.getCanonicalName(),
                identification,
                serviceInstanceId,
                applicationInfo);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof UpService.Info
                        && Objects.equals(getServiceType(), ((UpService.Info) object).getServiceType())
                        && Objects.equals(getIdentification(), ((UpService.Info) object).getIdentification())
                        && Objects.equals(getServiceInstanceId(), ((UpService.Info) object).getServiceInstanceId())
                        && Objects.equals(getApplicationInfo(), ((UpService.Info) object).getApplicationInfo())
                        && Objects.equals(getSpecificationName(), ((UpService.Info) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((UpService.Info) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return 7 + Objects.hashCode(specification) * 31
                + Objects.hashCode(serviceType) * 41
                + Objects.hashCode(identification) * 13
                + Objects.hashCode(serviceInstanceId) * 11
                + Objects.hashCode(applicationInfo) * 3;
    }

}
