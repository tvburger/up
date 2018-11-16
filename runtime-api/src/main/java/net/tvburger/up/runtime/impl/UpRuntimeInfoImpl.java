package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpRuntimeInfo;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.security.Identification;

import java.util.Objects;

public class UpRuntimeInfoImpl implements UpRuntimeInfo {

    public static final class Factory {

        public static UpRuntimeInfoImpl create(Identification identification, Specification specification) {
            Objects.requireNonNull(identification);
            Objects.requireNonNull(specification);
            return new UpRuntimeInfoImpl(identification, specification);
        }

        private Factory() {
        }

    }

    private final Identification identification;
    private final Specification specification;

    protected UpRuntimeInfoImpl(Identification identification, Specification specification) {
        this.identification = identification;
        this.specification = specification;
    }

    @Override
    public Identification getIdentification() {
        return identification;
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
        return String.format("UpRuntimeInfo{%s, %s}", identification, specification);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof UpRuntimeInfo
                        && Objects.equals(getIdentification(), ((UpRuntimeInfo) object).getIdentification())
                        && Objects.equals(getSpecificationName(), ((UpRuntimeInfo) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((UpRuntimeInfo) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return 22 + Objects.hashCode(identification) * 91
                + Objects.hashCode(specification) * 331;
    }

}
