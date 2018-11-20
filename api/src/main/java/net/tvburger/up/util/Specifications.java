package net.tvburger.up.util;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.SpecificationImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

public final class Specifications {

    public static final String UNVERSIONED = "unversioned";

    public static boolean isUnversioned(Specification specification) {
        return UNVERSIONED.equals(specification.getSpecificationVersion());
    }

    public static boolean definesClass(Specification specification, Class<?> clazz) {
        Objects.requireNonNull(specification);
        Objects.requireNonNull(clazz);
        return specification.getSpecificationName().equals(clazz.getCanonicalName())
                && specification.getSpecificationVersion().equals(getVersion(clazz));
    }

    public static Specification forClass(String className) {
        return forClass(className, UNVERSIONED);
    }

    public static Specification forClass(String className, String classVersion) {
        return SpecificationImpl.Factory.create(className, classVersion);
    }

    public static Specification forClass(Class<?> clazz) {
        return SpecificationImpl.Factory.create(clazz.getCanonicalName(), getVersion(clazz));
    }

    private static String getVersion(Class<?> clazz) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if ("serialVersionUID".equals(field.getName()) && Modifier.isStatic(field.getModifiers())) {
                    return Objects.toString(field.get(null));
                }
            }
            return UNVERSIONED;
        } catch (IllegalAccessException cause) {
            throw new IllegalStateException(cause);
        }
    }

    private Specifications() {
    }

}
