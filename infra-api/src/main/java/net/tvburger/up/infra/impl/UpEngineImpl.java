package net.tvburger.up.infra.impl;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.Objects;
import java.util.Set;

public class UpEngineImpl implements UpEngine {

    private final UpEngineManagerImpl manager;

    public UpEngineImpl(UpEngineManagerImpl manager) {
        this.manager = manager;
    }

    @Override
    public Set<Class<?>> listEndpointTypes() {
        return manager.listEndpointTypes();
    }

    @Override
    public Set<UpEndpointTechnologyInfo> listEndpointTechnologies() {
        return manager.listEndpointTechnologies();
    }

    @Override
    public <T, I extends UpEndpoint.Info> UpEndpointTechnology<I> getEndpointTechnology(Class<T> endpointType) {
        return manager.getEndpointTechnology(endpointType);
    }

    @Override
    public <I extends UpEndpoint.Info> UpEndpointTechnology<I> getEndpointTechnology(UpEndpointTechnologyInfo technologyInfo) {
        return manager.getEndpointTechnology(technologyInfo);
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

    @Override
    public int hashCode() {
        return Objects.hashCode(getInfo()) * 3 + 7;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof UpEngine)) {
            return false;
        }
        return Objects.equals(getInfo(), ((UpEngine) object).getInfo());
    }

}
