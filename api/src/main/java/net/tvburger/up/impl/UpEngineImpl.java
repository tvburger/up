package net.tvburger.up.impl;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.deploy.UpEngineInfo;
import net.tvburger.up.deploy.UpEngineManager;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.security.Identification;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UpEngineImpl implements UpEngine {

    public static final class Factory {

        public static UpEngineImpl create(UpEngineManager manager, Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> endpointTechnologies, UpRuntime runtime) {
            Objects.requireNonNull(manager);
            Objects.requireNonNull(endpointTechnologies);
            Objects.requireNonNull(runtime);
            return new UpEngineImpl(manager, Collections.unmodifiableMap(endpointTechnologies), runtime);
        }

        private Factory() {
        }

    }

    private final UpEngineManager manager;
    private final Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> endpointTechnologies;
    private final UpRuntime runtime;

    protected UpEngineImpl(UpEngineManager manager, Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> endpointTechnologies, UpRuntime runtime) {
        this.manager = manager;
        this.endpointTechnologies = endpointTechnologies;
        this.runtime = runtime;
    }

    @Override
    public UpEngineManager getManager() {
        return manager;
    }

    @Override
    public UpEngineInfo getInfo() {
        return manager.getInfo();
    }

    @Override
    public Set<EndpointTechnologyInfo<?>> getEndpointTechnologies() {
        return endpointTechnologies.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> EndpointTechnology<T> getEndpointTechnology(EndpointTechnologyInfo<T> info) {
        Objects.requireNonNull(info);
        return (EndpointTechnology<T>) endpointTechnologies.get(info);
    }

    @Override
    public UpRuntime getRuntime() {
        return runtime;
    }

    @Override
    public Identification getIdentification() {
        return manager.getInfo().getIdentification();
    }

    @Override
    public String toString() {
        return String.format("UpEngine{%s}", getInfo());
    }

}
