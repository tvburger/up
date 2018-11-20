package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpPackage;
import net.tvburger.up.behaviors.Specification;

import java.util.Objects;
import java.util.UUID;

public class UpPackageInfoImpl implements UpPackage.Info {

    private final UUID packageId;
    private final Specification specification;

    public UpPackageInfoImpl(UUID packageId, Specification specification) {
        this.packageId = packageId;
        this.specification = specification;
    }

    @Override
    public UUID getPackageId() {
        return packageId;
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
        return String.format("UpPackage.Info{%s, %s}",
                packageId,
                specification);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof UpPackage.Info
                        && Objects.equals(getPackageId(), ((UpPackage.Info) object).getPackageId())
                        && Objects.equals(getSpecificationName(), ((UpPackage.Info) object).getSpecificationName())
                        && Objects.equals(getSpecificationVersion(), ((UpPackage.Info) object).getSpecificationVersion()));
    }

    @Override
    public int hashCode() {
        return 723 + Objects.hashCode(packageId) * 31
                + Objects.hashCode(specification) * 411;
    }
}
