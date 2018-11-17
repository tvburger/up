package net.tvburger.up.clients.java.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.behaviors.Specification;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ClientSpecification implements Specification {

    private String specificationName;
    private String specificationVersion;

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
        return String.format("ClientSpecification{%s, %s}", specificationName, specificationVersion);
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
        return 13 + Objects.hashCode(specificationName) * 13
                + Objects.hashCode(specificationName) * 19;
    }

}
