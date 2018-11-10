package net.tvburger.up.impl;

import net.tvburger.up.Environment;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.UpRuntimeInfo;
import net.tvburger.up.runtime.UpRuntimeManager;
import net.tvburger.up.security.Identification;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UpRuntimeImpl implements UpRuntime {

    public static final class Factory {

        public static UpRuntimeImpl create(UpRuntimeManager manager, Set<UpEngine> engines, Map<String, Environment> environments) {
            Objects.requireNonNull(manager);
            Objects.requireNonNull(engines);
            Objects.requireNonNull(environments);
            return new UpRuntimeImpl(manager, Collections.unmodifiableSet(engines), Collections.unmodifiableMap(environments));
        }

        private Factory() {
        }

    }

    private final UpRuntimeManager manager;
    private final Set<UpEngine> engines;
    private final Map<String, Environment> environments;

    protected UpRuntimeImpl(UpRuntimeManager manager, Set<UpEngine> engines, Map<String, Environment> environments) {
        this.manager = manager;
        this.engines = engines;
        this.environments = environments;
    }

    @Override
    public UpRuntimeManager getManager() {
        return manager;
    }

    @Override
    public UpRuntimeInfo getInfo() {
        return manager.getInfo();
    }

    @Override
    public Set<UpEngine> getEngines() {
        return engines;
    }

    @Override
    public boolean hasEnvironment(String environmentName) {
        Objects.requireNonNull(environmentName);
        return environments.containsKey(environmentName);
    }

    @Override
    public Environment getEnvironment(String environmentName) {
        Objects.requireNonNull(environmentName);
        return environments.get(environmentName);
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
