package net.tvburger.up.runtime.impl;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.deploy.ResourceNotFoundException;
import net.tvburger.up.deploy.UpClassLoader;
import net.tvburger.up.deploy.UpResourceLoader;
import net.tvburger.up.util.Specifications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UpClassLoaderImpl extends ClassLoader implements UpClassLoader {

    private final Map<String, Class<?>> loadedClasses = new HashMap<>();
    private final UpResourceLoader loader;

    public UpClassLoaderImpl(UpResourceLoader loader) {
        this.loader = loader;
    }

    public UpClassLoaderImpl(ClassLoader parent, UpResourceLoader loader) {
        super(parent);
        this.loader = loader;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (loadedClasses.containsKey(name)) {
            return loadedClasses.get(name);
        }
        try {
            byte[] bytes = loadClassData(name);
            Class<?> loadedClass = defineClass(name, bytes, 0, bytes.length);
            loadedClasses.put(name, loadedClass);
            return loadedClass;
        } catch (IOException cause) {
            throw new ClassNotFoundException("Can't load class: " + name, cause);
        }
    }

    private byte[] loadClassData(String name) throws ClassNotFoundException, IOException {
        try (InputStream in = loader.loadResource(name.replace('.', '/') + ".class");
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (in == null) {
                throw new ClassNotFoundException("No class found: " + name);
            }
            byte[] buffer = new byte[1024 * 8];
            for (int i = in.read(buffer); i >= 0; i = in.read(buffer)) {
                out.write(buffer, 0, i);
            }
            return out.toByteArray();
        } catch (ResourceNotFoundException cause) {
            throw new ClassNotFoundException("Failed to load class: " + name, cause);
        }
    }

    @Override
    public Class<?> loadClass(Specification classSpecification) throws ClassNotFoundException {
        Class<?> loadedClass = findClass(classSpecification.getSpecificationName());
        if (!Specifications.definesClass(classSpecification, loadedClass)) {
            throw new ClassNotFoundException("Invalid version of class: " + loadedClass.getCanonicalName());
        }
        return loadedClass;
    }

}