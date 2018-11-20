package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpService;

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiServiceInfo<T> implements UpService.Info<T> {

    private Class<T> serviceType;
    private UUID serviceInstanceId;
    private ApiApplicationInfo applicationInfo;
    private ApiIdentification identification;
    private String specificationName;
    private String specificationVersion;

    @Override
    public Class<T> getServiceType() {
        return serviceType;
    }

    @Override
    public UUID getServiceInstanceId() {
        return serviceInstanceId;
    }

    @Override
    public ApiApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    @Override
    public ApiIdentification getIdentification() {
        return identification;
    }

    @Override
    public String getSpecificationName() {
        return specificationName;
    }

    @Override
    public String getSpecificationVersion() {
        return specificationVersion;
    }

    @Override
    public String toString() {
        return String.format("ApiServiceInfo{%s, %s, %s, %s, %s, %s}",
                specificationName,
                specificationVersion,
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
        return 11 + Objects.hashCode(specificationName) * 31
                + Objects.hashCode(specificationVersion) * 49
                + Objects.hashCode(serviceType) * 41
                + Objects.hashCode(identification) * 13
                + Objects.hashCode(serviceInstanceId) * 11
                + Objects.hashCode(applicationInfo) * 3;
    }
}
