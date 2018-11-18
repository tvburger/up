package net.tvburger.up.topology;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.util.Specifications;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;

public class ClassDefinition implements Serializable {

    public static final class Factory {

        public static ClassDefinition create(Class<?> clazz) {
            Objects.requireNonNull(clazz);
            return new ClassDefinition(
                    Specifications.forClass(clazz),
                    new byte[0]);
        }

        private Factory() {
        }

    }

    private final Specification classSpecification;
    private final byte[] bytes;

    protected ClassDefinition(Specification classSpecification, byte[] bytes) {
        this.classSpecification = classSpecification;
        this.bytes = bytes;
    }

    public Specification getClassSpecification() {
        return classSpecification;
    }

    public InputStream open() {
        return new ByteArrayInputStream(bytes);
    }

}
