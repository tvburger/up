package net.tvburger.up.impl;

import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.Specification;

import java.util.Objects;

public class ImplementationImpl implements Implementation {

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
    public String toString() {
        return String.format("Implementation{%s, %s, %s}", specification, name, version);
    }
}
