package net.tvburger.up.behaviors.impl;

import net.tvburger.up.behaviors.Specification;

import java.io.Serializable;
import java.util.Objects;

public class SpecificationImpl implements Specification, Serializable {

    public static final class Factory {

        public static SpecificationImpl create(String name, String version) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(version);
            return new SpecificationImpl(name, version);
        }

        private Factory() {
        }

    }

    private final String name;
    private final String version;

    protected SpecificationImpl(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getSpecificationName() {
        return name;
    }

    public String getSpecificationVersion() {
        return version;
    }

    @Override
    public String toString() {
        return String.format("Specification{%s, %s}", name, version);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof Specification
                        && Objects.equals(getSpecificationName(), ((Specification) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((Specification) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return 11 + Objects.hashCode(name) * 13
                + Objects.hashCode(version) * 19;
    }

}
