package net.tvburger.up.runtimes.local;

import net.tvburger.up.UpPackage;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.SpecificationImpl;
import net.tvburger.up.deploy.*;
import net.tvburger.up.runtime.impl.UpPackageInfoImpl;
import net.tvburger.up.runtime.impl.UpPackageManagerImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.util.Specifications;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class LocalPackageDefinition implements UpPackage, UpPackageDefinition {

    public static final class Factory {

        public static LocalPackageDefinition create(String name) {
            Objects.requireNonNull(name);
            return create(SpecificationImpl.Factory.create(name, Specifications.UNVERSIONED));
        }

        public static LocalPackageDefinition create(Specification specification) {
            Objects.requireNonNull(specification);
            UpPackage.Manager manager = new UpPackageManagerImpl(new UpPackageInfoImpl(UUID.randomUUID(), specification));
            return new LocalPackageDefinition(manager);
        }

        private Factory() {
        }

    }

    public static class ResourceLoader implements UpResourceLoader {

        @Override
        public boolean hasResource(String resourceName) {
            return getClass().getClassLoader().getResourceAsStream(resourceName) != null;
        }

        @Override
        public InputStream loadResource(String resourceName) throws ResourceNotFoundException, IOException {
            InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName);
            if (in == null) {
                throw new ResourceNotFoundException(resourceName);
            }
            return in;
        }

    }

    private final UpResourceLoader resourceLoader = new ResourceLoader();
    private final UpClassLoader classLoader = new LocalClassLoader();
    private final Manager manager;

    private LocalPackageDefinition(Manager manager) {
        this.manager = manager;
    }

    @Override
    public UpResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Override
    public UpClassLoader getClassLoader() {
        return classLoader;
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

}
