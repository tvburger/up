package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpRuntimeInfo;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UpRuntimeImpl implements UpRuntime {

    public static final class Factory {

        public static UpRuntimeImpl create(UpRuntime.Manager manager, Set<UpEngine.Info> engines, Map<String, UpEnvironment> environments) {
            Objects.requireNonNull(manager);
            Objects.requireNonNull(engines);
            Objects.requireNonNull(environments);
            return new UpRuntimeImpl(manager, Collections.unmodifiableSet(engines), Collections.unmodifiableMap(environments));
        }

        private Factory() {
        }

    }

    private final UpRuntime.Manager manager;
    private final Set<UpEngine.Info> engines;
    private final Map<String, UpEnvironment> environments;

    protected UpRuntimeImpl(UpRuntime.Manager manager, Set<UpEngine.Info> engines, Map<String, UpEnvironment> environments) {
        this.manager = manager;
        this.engines = engines;
        this.environments = environments;
    }

    @Override
    public UpRuntime.Manager getManager() {
        return manager;
    }

    @Override
    public UpRuntimeInfo getInfo() {
        return manager.getInfo();
    }

    @Override
    public Set<UpEngine.Info> listEngines() {
        return engines;
    }

    @Override
    public UpEngine.Manager getEngineManager(UpEngine.Info engineInfo) throws AccessDeniedException {
        return null;
    }

    @Override
    public Set<String> listEnvironments() {
        return environments.keySet();
    }

    @Override
    public boolean hasEnvironment(String environmentName) {
        Objects.requireNonNull(environmentName);
        return environments.containsKey(environmentName);
    }

    @Override
    public UpEnvironment getEnvironment(String environmentName) {
        Objects.requireNonNull(environmentName);
        return environments.get(environmentName);
    }

    @Override
    public Set<UpEndpointTechnology.Info<?>> listEndpointTechnologies() {
        return null;
    }

    @Override
    public <I> UpEndpointTechnology.Manager<I> getEndpointTechnologyManager(UpEndpointTechnology.Info<I> endpointInfo) throws AccessDeniedException {
        return null;
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
