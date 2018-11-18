package net.tvburger.up.applications.api.types;

import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.runtime.UpEndpointTechnology;

import java.util.Objects;

public final class ApiEndpointTechnologyInfo implements UpEndpointTechnologyInfo {

    private Class<?> endpointType;
    private String specificationName;
    private String specificationVersion;

    @Override
    public Class<?> getEndpointType() {
        return endpointType;
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
        return String.format("ApiEndpointTechnologyInfo{%s, %s, %s}", endpointType.getCanonicalName(), specificationName, specificationVersion);
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
