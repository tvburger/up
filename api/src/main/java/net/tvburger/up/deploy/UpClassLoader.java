package net.tvburger.up.deploy;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.util.Specifications;

public interface UpClassLoader {

    default <T> Class<? extends T> loadClass(String className, Class<T> typeClass) throws ClassNotFoundException {
        return loadClass(Specifications.forClass(className), typeClass);
    }

    @SuppressWarnings("unchecked")
    default <T> Class<? extends T> loadClass(Specification classSpecification, Class<T> typeClass) throws ClassNotFoundException {
        Class<?> loadedClass = loadClass(classSpecification);
        if (!typeClass.isAssignableFrom(loadedClass)) {
            throw new ClassNotFoundException("Invalid type of class: " + loadedClass.getCanonicalName());
        }
        return (Class<? extends T>) loadedClass;
    }

    default Class<?> loadClass(String className) throws ClassNotFoundException {
        return loadClass(Specifications.forClass(className));
    }

    Class<?> loadClass(Specification classSpecification) throws ClassNotFoundException;

}
