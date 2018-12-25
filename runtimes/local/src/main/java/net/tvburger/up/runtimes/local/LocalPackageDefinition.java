package net.tvburger.up.runtimes.local;

import net.tvburger.up.UpPackage;
import net.tvburger.up.deploy.*;
import net.tvburger.up.runtime.impl.UpPackageInfoImpl;
import net.tvburger.up.runtime.impl.UpPackageManagerImpl;
import net.tvburger.up.runtimes.local.impl.LocalClassLoader;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.util.Specifications;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class LocalPackageDefinition implements UpPackage, UpPackageDefinition {

    private static final LocalPackageDefinition INSTANCE = new LocalPackageDefinition(
            LocalPackageDefinition.class.getClassLoader(),
            new UpPackageManagerImpl(new UpPackageInfoImpl(UUID.randomUUID(),
                    Specifications.forClass(LocalPackageDefinition.class))));

    public static LocalPackageDefinition get() {
        return INSTANCE;
    }

    public class ResourceLoader implements UpResourceLoader {

        @Override
        public boolean hasResource(String resourceName) {
            return classLoader.getResourceAsStream(resourceName) != null;
        }

        @Override
        public InputStream loadResource(String resourceName) throws ResourceNotFoundException, IOException {
            InputStream in = classLoader.getResourceAsStream(resourceName);
            if (in == null) {
                throw new ResourceNotFoundException(resourceName);
            }
            return in;
        }

    }

    private final ClassLoader classLoader;
    private final Manager manager;
    private final UpClassLoader upClassLoader;
    private final UpResourceLoader upResourceLoader = new ResourceLoader();

    private LocalPackageDefinition(ClassLoader classLoader, Manager manager) {
        this.classLoader = classLoader;
        this.manager = manager;
        this.upClassLoader = new LocalClassLoader(classLoader);
    }

    @Override
    public UpResourceLoader getResourceLoader() {
        return upResourceLoader;
    }

    @Override
    public UpClassLoader getClassLoader() {
        return upClassLoader;
    }

    @Override
    public Set<UpApplicationDefinition> getApplicationDefinitions() {
        return Collections.emptySet();
    }

    @Override
    public Manager getManager() throws AccessDeniedException {
        return manager;
    }

    @Override
    public Info getInfo() {
        return manager.getInfo();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInfo()) * 3 + 7;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof UpPackage)) {
            return false;
        }
        return Objects.equals(getInfo(), ((UpPackage) object).getInfo());
    }

}
