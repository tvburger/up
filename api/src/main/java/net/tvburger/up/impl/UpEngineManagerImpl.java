package net.tvburger.up.impl;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.Up;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.UpContext;
import net.tvburger.up.runtime.*;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.topology.UpEngineDefinition;
import net.tvburger.up.util.EndpointTechnologyLoader;
import net.tvburger.up.util.LocalJavaImplementation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UpEngineManagerImpl extends LifecycleManagerImpl implements UpEngineManager {

    public static final class Factory {

        public static UpEngineManagerImpl create(UpEngineInfo info, UpEngineDefinition engineDefinition, Identity identity, UpRuntime runtime) {
            Objects.requireNonNull(info);
            Objects.requireNonNull(engineDefinition);
            Objects.requireNonNull(identity);
            Objects.requireNonNull(runtime);
            return new UpEngineManagerImpl(info, engineDefinition, identity, runtime);
        }

        private Factory() {
        }

    }

    private final UpEngineInfo info;
    private final UpEngineDefinition engineDefinition;
    private final Identity identity;
    private final Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> endpointTechnologies = new HashMap<>();
    private final Implementation implementation = LocalJavaImplementation.get();
    private UpEngine engine;
    private UpRuntime runtime;

    protected UpEngineManagerImpl(UpEngineInfo info, UpEngineDefinition engineDefinition, Identity identity, UpRuntime runtime) {
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

    public Set<EndpointTechnologyInfo<?>> getEndpointTechnologies() {
        return endpointTechnologies.keySet();
    }

    public UpRuntime getRuntime() {
        return runtime;
    }

    @SuppressWarnings("unchecked")
    public <T> EndpointTechnology<T> getEndpointTechnology(EndpointTechnologyInfo<T> info) {
        return (EndpointTechnology<T>) endpointTechnologies.get(info);
    }

    @Override
    public UpEngineInfo getInfo() {
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
        UpContext context = Up.getContext();
        try {
            if (!implementation.equals(engineDefinition.getEngineImplementation())) {
                throw new LifecycleException("Unsupported implementation: " + engineDefinition.getEngineImplementation());
            }
            UpContextImpl engineContext = new UpContextImpl();
            engineContext.setIdentity(identity);
            engineContext.setLocality(Locality.Factory.create(runtime.getInfo(), info));
            Up.setContext(engineContext);
            endpointTechnologies.putAll(EndpointTechnologyLoader.load(engine, identity, engineDefinition.getEndpointImplementations()));
            for (EndpointTechnology<?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().init();
            }
        } catch (DeployException | AccessDeniedException cause) {
            throw new LifecycleException(cause);
        } finally {
            Up.setContext(context);
        }
    }

    @Override
    public void start() throws LifecycleException {
        super.start();
        UpContext context = Up.getContext();
        try {
            UpContextImpl engineContext = new UpContextImpl();
            engineContext.setIdentity(identity);
            engineContext.setLocality(Locality.Factory.create(runtime.getInfo(), info));
            Up.setContext(engineContext);
            for (EndpointTechnology<?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().start();
            }
        } catch (AccessDeniedException cause) {
            throw new LifecycleException(cause);
        } finally {
            Up.setContext(context);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        super.stop();
        UpContext context = Up.getContext();
        try {
            UpContextImpl engineContext = new UpContextImpl();
            engineContext.setIdentity(identity);
            engineContext.setLocality(Locality.Factory.create(runtime.getInfo(), info));
            Up.setContext(engineContext);
            for (EndpointTechnology<?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().stop();
            }
        } catch (AccessDeniedException cause) {
            throw new LifecycleException(cause);
        } finally {
            Up.setContext(context);
        }
    }

    @Override
    public void destroy() throws LifecycleException {
        super.destroy();
        UpContext context = Up.getContext();
        try {
            UpContextImpl engineContext = new UpContextImpl();
            engineContext.setIdentity(identity);
            engineContext.setLocality(Locality.Factory.create(runtime.getInfo(), info));
            Up.setContext(engineContext);
            for (EndpointTechnology<?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().destroy();
            }
        } catch (AccessDeniedException cause) {
            throw new LifecycleException(cause);
        } finally {
            Up.setContext(context);
        }
    }

}
