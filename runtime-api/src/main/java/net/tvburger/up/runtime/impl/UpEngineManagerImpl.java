package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.util.UpEndpointTechnologies;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.topology.UpEngineDefinition;
import net.tvburger.up.util.LocalJavaImplementation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UpEngineManagerImpl extends LifecycleManagerImpl implements UpEngine.Manager {

    public static final class Factory {

        public static UpEngineManagerImpl create(UpEngine.Info info, UpEngineDefinition engineDefinition, Identity identity, UpRuntime runtime) {
            Objects.requireNonNull(info);
            Objects.requireNonNull(engineDefinition);
            Objects.requireNonNull(identity);
            Objects.requireNonNull(runtime);
            return new UpEngineManagerImpl(info, engineDefinition, identity, runtime);
        }

        private Factory() {
        }

    }

    private final UpEngine.Info info;
    private final UpEngineDefinition engineDefinition;
    private final Identity identity;
    private final Map<Class<?>, UpEndpointTechnology<?, ?>> endpointTechnologies = new HashMap<>();
    private final Implementation implementation = LocalJavaImplementation.get();
    private UpEngine engine;
    private UpRuntime runtime;

    protected UpEngineManagerImpl(UpEngine.Info info, UpEngineDefinition engineDefinition, Identity identity, UpRuntime runtime) {
        this.info = info;
        this.engineDefinition = engineDefinition;
        this.identity = identity;
        this.runtime = runtime;
    }

    public UpEngine getEngine() {
        return engine;
    }

    public void setEngine(UpEngine engine) {
        this.engine = engine;
    }

    public Identity getIdentity() {
        return identity;
    }

    public Set<Class<?>> getEndpointTechnologies() {
        return endpointTechnologies.keySet();
    }

    public UpRuntime getRuntime() {
        return runtime;
    }

    @SuppressWarnings("unchecked")
    public <T, I extends UpEndpoint.Info> UpEndpointTechnology<T, I> getEndpointTechnology(Class<T> endpointType) {
        return (UpEndpointTechnology<T, I>) endpointTechnologies.get(endpointType);
    }

    @Override
    public UpEngine.Info getInfo() {
        return info;
    }

    @Override
    public String getImplementationName() {
        return implementation.getImplementationName();
    }

    @Override
    public String getImplementationVersion() {
        return implementation.getImplementationVersion();
    }

    @Override
    public Specification getSpecification() {
        return implementation.getSpecification();
    }

    @Override
    public void init() throws LifecycleException {
        super.init();
        try {
            if (!implementation.equals(engineDefinition.getEngineImplementation())) {
                throw new LifecycleException("Unsupported implementation: " + engineDefinition.getEngineImplementation());
            }
            endpointTechnologies.putAll(UpEndpointTechnologies.load(engineDefinition.getEndpointImplementations()));
            for (UpEndpointTechnology<?, ?> endpointTechnology : endpointTechnologies.values()) {
                if (!implementation.getSpecification().equals(endpointTechnology.getEngineRequirement())) {
                    throw new TopologyException("Unsupported UpEndpointTechnology: " + endpointTechnology.getInfo());
                }
                endpointTechnology.getManager().init();
            }
        } catch (TopologyException | AccessDeniedException cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void start() throws LifecycleException {
        super.start();
        try {
            for (UpEndpointTechnology<?, ?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().start();
            }
        } catch (AccessDeniedException cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        super.stop();
        try {
            for (UpEndpointTechnology<?, ?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().stop();
            }
        } catch (AccessDeniedException cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void destroy() throws LifecycleException {
        super.destroy();
        try {
            for (UpEndpointTechnology<?, ?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().destroy();
            }
        } catch (AccessDeniedException cause) {
            throw new LifecycleException(cause);
        }
    }

}
