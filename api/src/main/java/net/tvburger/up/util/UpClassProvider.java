package net.tvburger.up.util;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.topology.TopologyException;

public final class UpClassProvider {

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> getClass(Specification specification, Class<T> typeClass) throws TopologyException {
        Class<?> clazz = getClass(specification);
        if (!typeClass.isAssignableFrom(clazz)) {
            throw new TopologyException("Class is not of type: " + typeClass.getCanonicalName());
        }
        return (Class<? extends T>) clazz;
    }

    public static Class<?> getClass(Specification specification) throws TopologyException {
        try {
            return Class.forName(specification.getSpecificationName());
        } catch (ClassNotFoundException cause) {
            throw new TopologyException("Failed to load class: " + specification.getSpecificationName(), cause);
        }
    }

    private UpClassProvider() {
    }

}
