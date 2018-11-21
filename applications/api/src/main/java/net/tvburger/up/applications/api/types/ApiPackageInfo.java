package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpPackage;

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiPackageInfo implements UpPackage.Info {

    public static ApiPackageInfo fromUp(UpPackage.Info up) {
        ApiPackageInfo api = new ApiPackageInfo();
        api.packageId = up.getPackageId();
        api.specificationName = up.getSpecificationName();
        api.specificationVersion = up.getSpecificationVersion();
        return api;
    }

    public UpPackage.Info toUp() {
        return this;
    }

    private UUID packageId;
    private String specificationName;
    private String specificationVersion;

    @Override
    public UUID getPackageId() {
        return packageId;
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
        return String.format("UpPackage.Info{%s, %s, %s}",
                packageId,
                specificationName,
                specificationVersion);
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
        return 223 + Objects.hashCode(packageId) * 31
                + Objects.hashCode(specificationName) * 411
                + Objects.hashCode(specificationVersion) * 111;
    }

}
