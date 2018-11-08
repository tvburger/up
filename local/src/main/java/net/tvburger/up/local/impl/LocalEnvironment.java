package net.tvburger.up.local.impl;

import net.tvburger.up.*;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.Objects;

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
    public <T> EndpointTechnology<T> getEndpointTechnology(EndpointTechnologyInfo<T> info) throws AccessDeniedException, DeployException {
        return engine.getEndpointTechnology(info);
    }

}
