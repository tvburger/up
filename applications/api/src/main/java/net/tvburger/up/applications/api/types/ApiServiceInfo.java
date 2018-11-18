package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.security.Identification;

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiServiceInfo<T> implements UpService.Info<T> {

    private Class<T> serviceType;
    private UUID serviceInstanceId;
    private ApiEnvironmentInfo environmentInfo;
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
    public UpEnvironment.Info getEnvironmentInfo() {
        return environmentInfo;
    }

    @Override
    public Identification getIdentification() {
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
        return 11 + Objects.hashCode(specificationName) * 31
                + Objects.hashCode(specificationVersion) * 49
                + Objects.hashCode(serviceType) * 41
                + Objects.hashCode(identification) * 13
                + Objects.hashCode(serviceInstanceId) * 11
                + Objects.hashCode(environmentInfo) * 3;
    }
}
