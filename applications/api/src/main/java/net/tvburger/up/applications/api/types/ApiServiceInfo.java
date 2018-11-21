package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.impl.SpecificationImpl;
import net.tvburger.up.runtime.impl.UpServiceInfoImpl;
import net.tvburger.up.util.Specifications;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiServiceInfo {

    public static ApiServiceInfo fromUp(UpService.Info<?> up) {
        ApiServiceInfo api = new ApiServiceInfo();
        api.serviceType = ApiSpecification.fromUp(Specifications.forClass(up.getServiceType()));
        api.serviceInstanceId = up.getServiceInstanceId();
        api.applicationInfo = ApiApplicationInfo.fromUp(up.getApplicationInfo());
        api.identification = ApiIdentification.fromUp(up.getIdentification());
        api.specificationName = up.getSpecificationName();
        api.specificationVersion = up.getSpecificationVersion();
        return api;
    }

    @SuppressWarnings("unchecked")
    public UpService.Info<?> toUp() throws ClassNotFoundException {
        return UpServiceInfoImpl.Factory.create(
                ApiSpecification.fromUp(SpecificationImpl.Factory.create(specificationName, specificationVersion)),
                (Class) Class.forName(serviceType.getSpecificationName()),
                identification,
                serviceInstanceId,
                applicationInfo);
    }

    private ApiSpecification serviceType;
    private UUID serviceInstanceId;
    private ApiApplicationInfo applicationInfo;
    private ApiIdentification identification;
    private String specificationName;
    private String specificationVersion;

    public ApiSpecification getServiceType() {
        return serviceType;
    }

    public UUID getServiceInstanceId() {
        return serviceInstanceId;
    }

    public ApiApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public ApiIdentification getIdentification() {
        return identification;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public String getSpecificationVersion() {
        return specificationVersion;
    }

    @Override
    public String toString() {
        return String.format("ApiServiceInfo{%s, %s, %s, %s, %s, %s}",
                specificationName,
                specificationVersion,
                serviceType,
                identification,
                serviceInstanceId,
                applicationInfo);
    }

}
