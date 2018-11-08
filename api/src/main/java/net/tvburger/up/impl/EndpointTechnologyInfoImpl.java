package net.tvburger.up.impl;

import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.behaviors.Specification;

import java.util.Objects;

public class EndpointTechnologyInfoImpl<T> implements EndpointTechnologyInfo<T> {

    private final Class<T> endpointType;
    private final Specification specification;

    protected EndpointTechnologyInfoImpl(Class<T> endpointType, Specification specification) {
        this.endpointType = endpointType;
        this.specification = specification;
    }

    @Override
    public Class<T> getEndpointType() {
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
        return String.format("EnvironmentInfo{%s, %s}", endpointType, specification);
    }

    @Override
    public boolean equals(Object object) {
        return this == object
                || null != object &&
                (object instanceof EndpointTechnologyInfo
                        && Objects.equals(getEndpointType(), ((EndpointTechnologyInfo) object).getEndpointType())
                        && Objects.equals(getSpecificationName(), ((EndpointTechnologyInfo) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((EndpointTechnologyInfo) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return 3 + Objects.hashCode(endpointType) * 51
                + Objects.hashCode(specification) * 97;
    }

}
