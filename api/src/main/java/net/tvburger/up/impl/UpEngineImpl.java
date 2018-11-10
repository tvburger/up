package net.tvburger.up.impl;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpEngineInfo;
import net.tvburger.up.runtime.UpEngineManager;
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
    public Set<EndpointTechnologyInfo<?>> getEndpointTechnologies() throws AccessDeniedException {
        return manager.getEndpointTechnologies();
    }

    @Override
    public <T> EndpointTechnology<T> getEndpointTechnology(EndpointTechnologyInfo<T> info) throws AccessDeniedException {
        return manager.getEndpointTechnology(info);
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
    public UpEngineManager getManager() throws AccessDeniedException {
        return manager;
    }

    @Override
    public UpEngineInfo getInfo() {
        return manager.getInfo();
    }

}
