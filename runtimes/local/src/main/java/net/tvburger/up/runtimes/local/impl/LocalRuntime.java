package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.UpEndpointTechnologyInfo;
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

public final class LocalRuntime implements UpRuntime {

    public static final class Factory {

        public static LocalRuntime create(LocalRuntimeManager manager, Set<UpEngine.Info> engines, Map<String, UpEnvironment> environments) {
            Objects.requireNonNull(manager);
            Objects.requireNonNull(engines);
            Objects.requireNonNull(environments);
            return new LocalRuntime(manager, Collections.unmodifiableSet(engines), Collections.unmodifiableMap(environments));
        }

        private Factory() {
        }

    }

    private final LocalRuntimeManager manager;
    private final Set<UpEngine.Info> engines;
    private final Map<String, UpEnvironment> environments;

    private LocalRuntime(LocalRuntimeManager manager, Set<UpEngine.Info> engines, Map<String, UpEnvironment> environments) {
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
    public Set<UpEndpointTechnologyInfo> listEndpointTechnologies() {
        return manager.getEngine().listEndpointTechnologies();
    }

    @Override
    public UpEndpointTechnology.Manager<?> getEndpointTechnologyManager(UpEndpointTechnologyInfo endpointInfo) throws AccessDeniedException {
        return manager.getEngine().getEndpointTechnology(endpointInfo).getManager();
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
