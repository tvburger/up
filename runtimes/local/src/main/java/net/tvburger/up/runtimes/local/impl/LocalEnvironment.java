package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class LocalEnvironment implements Environment {

    public static final class Factory {

        public static LocalEnvironment create(UpEngine engine, LocalEnvironmentManager manager) {
            Objects.requireNonNull(engine);
            Objects.requireNonNull(manager);
            return new LocalEnvironment(engine, manager);
        }

        private Factory() {
        }

    }

    private final UpEngine engine;
    private final LocalEnvironmentManager manager;

    private LocalEnvironment(UpEngine engine, LocalEnvironmentManager manager) {
        this.engine = engine;
        this.manager = manager;
    }

    @Override
    public EnvironmentManager getManager() {
        return manager;
    }

    @Override
    public EnvironmentInfo getInfo() {
        return manager.getInfo();
    }

    @Override
    public Identification getIdentification() {
        return manager.getInfo().getIdentification();
    }

    @Override
    public <T> Service<T> getService(Class<T> serviceType) throws AccessDeniedException, DeployException {
        return manager.getLocalServicesManager().getService(serviceType);
    }

    @Override
    public Set<Service<?>> getServices() throws AccessDeniedException {
        return manager.getLocalServicesManager().getServices();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Endpoint> getEndpoints(Specification endpointTechnology) throws AccessDeniedException, DeployException {
        Objects.requireNonNull(endpointTechnology);
        for (EndpointTechnologyInfo<?> info : engine.getEndpointTechnologies()) {
            if (endpointTechnology.equals(info)) {
                return (Set<Endpoint>) engine.getEndpointTechnology(info).getEndpoints(getInfo());
            }
        }
        return Collections.emptySet();
    }

    @Override
    public UpRuntime getRuntime() throws AccessDeniedException {
        return engine.getRuntime();
    }

}
