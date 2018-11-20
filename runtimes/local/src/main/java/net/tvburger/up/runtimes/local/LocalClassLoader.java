package net.tvburger.up.runtimes.local;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.deploy.UpClassLoader;
import net.tvburger.up.util.Specifications;

public final class LocalClassLoader implements UpClassLoader {

    @Override
    public Class<?> loadClass(Specification classSpecification) throws ClassNotFoundException {
        Class<?> loadedClass = getClass().getClassLoader().loadClass(classSpecification.getSpecificationName());
        if (!Specifications.definesClass(classSpecification, loadedClass)) {
            throw new ClassNotFoundException("Invalid version of class: " + loadedClass.getCanonicalName());
        }
        return loadedClass;
    }

}
