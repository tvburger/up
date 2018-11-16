package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.Set;

public class UpEngineImpl implements UpEngine {

    private final UpEngineManagerImpl manager;

    public UpEngineImpl(UpEngineManagerImpl manager) {
        this.manager = manager;
    }

    @Override
    public Set<Class<?>> listEndpointTypes() {
        return manager.getEndpointTechnologies();
    }

    @Override
    public <T, I extends UpEndpoint.Info> UpEndpointTechnology<T, I> getEndpointTechnology(Class<T> endpointType) {
        return manager.getEndpointTechnology(endpointType);
    }

    @Override
    public UpRuntime getRuntime() {
        return manager.getRuntime();
    }

    @Override
    public Identification getIdentification() {
        return manager.getInfo().getIdentification();
    }

    @Override
    public UpEngine.Manager getManager() throws AccessDeniedException {
        return manager;
    }

    @Override
    public UpEngine.Info getInfo() {
        return manager.getInfo();
    }

}
