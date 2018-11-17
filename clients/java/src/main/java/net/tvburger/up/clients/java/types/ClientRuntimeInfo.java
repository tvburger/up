package net.tvburger.up.clients.java.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpRuntimeInfo;
import net.tvburger.up.security.Identification;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ClientRuntimeInfo implements UpRuntimeInfo {

    private ClientIdentification identification;
    private String specificationName;
    private String specificationVersion;

    @Override
    public Identification getIdentification() {
        return identification;
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
        return String.format("ClientRuntimeInfo{%s, %s, %s}", identification, specificationName, specificationVersion);
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
        return 27 + Objects.hashCode(identification) * 91
                + Objects.hashCode(specificationName) * 331
                + Objects.hashCode(specificationVersion) * 31;
    }
}
