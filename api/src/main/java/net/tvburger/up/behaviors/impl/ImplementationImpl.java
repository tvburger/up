package net.tvburger.up.behaviors.impl;

import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.ValueObject;

import java.util.Objects;

public class ImplementationImpl implements Implementation, ValueObject {

    public static final class Factory {

        public static ImplementationImpl create(Specification specification, String name, String version) {
            Objects.requireNonNull(specification);
            Objects.requireNonNull(name);
            Objects.requireNonNull(version);
            return new ImplementationImpl(specification, name, version);
        }

        private Factory() {
        }

    }

    private final Specification specification;
    private final String name;
    private final String version;

    protected ImplementationImpl(Specification specification, String name, String version) {
        this.specification = specification;
        this.name = name;
        this.version = version;
    }

    @Override
    public String getImplementationName() {
        return name;
    }

    @Override
    public String getImplementationVersion() {
        return version;
    }

    @Override
    public Specification getSpecification() {
        return specification;
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof Implementation
                        && Objects.equals(getSpecification(), ((Implementation) object).getSpecification())
                        && Objects.equals(getImplementationName(), ((Implementation) object).getImplementationName())
                        && Objects.equals(getImplementationVersion(), ((Implementation) object).getImplementationVersion()));
    }

    @Override
    public int hashCode() {
        return 17 + Objects.hashCode(specification) * 37
                + Objects.hashCode(name) * 43
                + Objects.hashCode(version) * 3;
    }

    @Override
    public String toString() {
        return String.format("Implementation{%s, %s, %s}", specification, name, version);
    }
}
