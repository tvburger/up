package net.tvburger.up.applications.api.types;

import net.tvburger.up.UpPackage;
import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.deploy.UpClassLoader;
import net.tvburger.up.deploy.UpResourceLoader;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Objects;
import java.util.Set;

public final class ApiPackage implements UpPackage {

    private final UpPackage.Manager manager;
    private final ApiPackageDefinition definition;

    public ApiPackage(Manager manager, ApiPackageDefinition definition) {
        this.manager = manager;
        this.definition = definition;
    }

    @Override
    public UpResourceLoader getResourceLoader() {
        return null;
    }

    @Override
    public UpClassLoader getClassLoader() {
        return null;
    }

    @Override
    public Set<UpApplicationDefinition> getApplicationDefinitions() {
        return null;
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
