package net.tvburger.up.applications.api.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class UpApplicationClassLoader extends ClassLoader {

    private final UpApplicationResourceLoader loader;

    public UpApplicationClassLoader(UpApplicationResourceLoader loader) {
        this.loader = loader;
    }

    public UpApplicationClassLoader(ClassLoader parent, UpApplicationResourceLoader loader) {
        super(parent);
        this.loader = loader;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] bytes = loadClassData(name);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException cause) {
            throw new ClassNotFoundException("Can't load class: " + name, cause);
        }
    }

    private byte[] loadClassData(String name) throws ClassNotFoundException, IOException {
        try (InputStream in = loader.openResource(name.replace('.', '/'));
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (in == null) {
                throw new ClassNotFoundException("No class found: " + name);
            }
            byte[] buffer = new byte[1024 * 8];
            for (int i = in.read(buffer); i >= 0; i = in.read(buffer)) {
                out.write(buffer, 0, i);
            }
            return out.toByteArray();
        }
    }

}