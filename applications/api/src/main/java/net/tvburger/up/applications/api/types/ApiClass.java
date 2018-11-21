package net.tvburger.up.applications.api.types;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.SpecificationImpl;
import net.tvburger.up.deploy.UpClassLoader;
import net.tvburger.up.util.Specifications;

public class ApiClass {

    public static final class ClassNotPresent {
    }

    public static ApiClass fromUp(Class<?> up) {
        Specification specification = Specifications.forClass(up);
        ApiClass api = new ApiClass();
        api.className = specification.getSpecificationName();
        api.classVersion = specification.getSpecificationVersion();
        return api;
    }

    public Class<?> toUp(UpClassLoader loader) {
        try {
            if (loader == null) {
                return Class.forName(className);
            } else {
                return loader.loadClass(SpecificationImpl.Factory.create(className, classVersion));
            }
        } catch (ClassNotFoundException cause) {
            return ClassNotPresent.class;
        }
    }

    private String className;
    private String classVersion;

    public String getClassName() {
        return className;
    }

    public String getClassVersion() {
        return classVersion;
    }
}
