package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.deploy.UpClassLoader;
import net.tvburger.up.util.Specifications;

public final class LocalClassLoader implements UpClassLoader {

    private final ClassLoader classLoader;

    public LocalClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Class<?> loadClass(Specification classSpecification) throws ClassNotFoundException {
        Class<?> loadedClass = classLoader.loadClass(classSpecification.getSpecificationName());
        if (!Specifications.definesClass(classSpecification, loadedClass)) {
            throw new ClassNotFoundException("Invalid version of class: " + loadedClass.getCanonicalName());
        }
        return loadedClass;
    }

}
