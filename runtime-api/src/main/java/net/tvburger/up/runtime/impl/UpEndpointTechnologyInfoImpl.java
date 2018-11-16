package net.tvburger.up.runtime.impl;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.UpEndpointTechnology;

import java.util.Objects;

public class UpEndpointTechnologyInfoImpl<T> implements UpEndpointTechnology.Info<T> {

    private final Class<T> endpointType;
    private final Specification specification;

    protected UpEndpointTechnologyInfoImpl(Class<T> endpointType, Specification specification) {
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
        return String.format("UpEnvironment.Info{%s, %s}", endpointType, specification);
    }

    @Override
    public boolean equals(Object object) {
        return this == object
                || null != object &&
                (object instanceof UpEndpointTechnology.Info
                        && Objects.equals(getEndpointType(), ((UpEndpointTechnology.Info) object).getEndpointType())
                        && Objects.equals(getSpecificationName(), ((UpEndpointTechnology.Info) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((UpEndpointTechnology.Info) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return 3 + Objects.hashCode(endpointType) * 51
                + Objects.hashCode(specification) * 97;
    }

}
