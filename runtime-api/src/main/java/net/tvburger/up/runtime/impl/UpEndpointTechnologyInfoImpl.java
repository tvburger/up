package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.UpEndpointTechnology;

import java.util.Objects;

public class UpEndpointTechnologyInfoImpl implements UpEndpointTechnologyInfo {

    private final Class<?> endpointType;
    private final Specification specification;

    protected UpEndpointTechnologyInfoImpl(Class<?> endpointType, Specification specification) {
        this.endpointType = endpointType;
        this.specification = specification;
    }

    @Override
    public Class<?> getEndpointType() {
        return endpointType;
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
        return String.format("UpEndpointTechnologyInfo{%s, %s}", endpointType.getCanonicalName(), specification);
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
        return 3 + Objects.hashCode(endpointType) * 51
                + Objects.hashCode(specification) * 97;
    }

}
