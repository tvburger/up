package net.tvburger.up.applications.api.types;

import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.runtime.UpEndpointTechnology;

import java.util.Objects;

public final class ApiEndpointTechnologyInfo implements UpEndpointTechnologyInfo {

    public static ApiEndpointTechnologyInfo fromUp(UpEndpointTechnologyInfo up) {
        ApiEndpointTechnologyInfo api = new ApiEndpointTechnologyInfo();
        api.endpointType = up.getEndpointType();
        api.specificationName = up.getSpecificationName();
        api.specificationVersion = up.getSpecificationVersion();
        return api;
    }

    public UpEndpointTechnologyInfo toUp() {
        return this;
    }

    private Class<?> endpointType;
    private String specificationName;
    private String specificationVersion;

    public Class<?> getEndpointType() {
        return endpointType;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public String getSpecificationVersion() {
        return specificationVersion;
    }

    @Override
    public String toString() {
        return String.format("ApiEndpointTechnologyInfo{%s, %s, %s}", endpointType.getName(), specificationName, specificationVersion);
    }

    @Override
    public boolean equals(Object object) {
        return this == object
                || null != object &&
                (object instanceof UpEndpointTechnology.Info
                        && Objects.equals(getEndpointType(), ((UpEndpointTechnologyInfo) object).getEndpointType())
                        && Objects.equals(getSpecificationName(), ((UpEndpointTechnologyInfo) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((UpEndpointTechnologyInfo) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return 82 + Objects.hashCode(endpointType) * 51
                + Objects.hashCode(specificationName) * 97
                + Objects.hashCode(specificationVersion) * 92;
    }

}
