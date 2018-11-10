package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.Up;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.UpContext;
import net.tvburger.up.topology.UpEngineDefinition;
import net.tvburger.up.runtime.*;
import net.tvburger.up.util.LocalJavaImplementation;
import net.tvburger.up.impl.UpContextImpl;
import net.tvburger.up.impl.UpEngineImpl;
import net.tvburger.up.impl.UpEngineInfoImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.EndpointTechnologyLoader;
import net.tvburger.up.util.Identities;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class LocalUpEngineManager implements UpEngineManager {

    public static final class Factory {

        public static LocalUpEngineManager create(UpRuntime runtime, UpEngineDefinition engineDefinition, Identity identity) throws UnknownHostException, LifecycleException {
            LocalUpEngineManager manager = new LocalUpEngineManager(
                    UpEngineInfoImpl.Factory.create(
                            UUID.randomUUID(),
                            InetAddress.getLocalHost(),
                            -1,
                            Identities.ANONYMOUS,
                            LocalJavaImplementation.get().getSpecification()),
                    engineDefinition,
                    identity,
                    runtime);
            manager.init();
            return manager;
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

    private LocalUpEngineManager(UpEngineInfo info, UpEngineDefinition engineDefinition, Identity identity, UpRuntime runtime) {
        this.info = info;
        this.engineDefinition = engineDefinition;
        this.identity = identity;
        this.runtime = runtime;
    }

    Identity getIdentity() {
        return identity;
    }

    public UpEngine getEngine() {
        return engine;
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
        UpContext serviceContext = Up.getContext();
        try {
            if (!implementation.equals(engineDefinition.getEngineImplementation())) {
                throw new LifecycleException("Unsupported implementation: " + engineDefinition.getEngineImplementation());
            }
            engine = UpEngineImpl.Factory.create(this, endpointTechnologies, runtime);
            UpContextImpl engineContext = new UpContextImpl();
            engineContext.setIdentity(identity);
            engineContext.setLocality(Locality.Factory.create(engine));
            Up.setContext(engineContext);
            endpointTechnologies.putAll(EndpointTechnologyLoader.load(engine, identity, engineDefinition.getEndpointImplementations()));
            for (EndpointTechnology<?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().init();
            }
        } catch (DeployException | AccessDeniedException cause) {
            throw new LifecycleException(cause);
        } finally {
            Up.setContext(serviceContext);
        }
    }

    @Override
    public void start() throws LifecycleException {
        try {
            for (EndpointTechnology<?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().start();
            }
        } catch (AccessDeniedException cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        try {
            for (EndpointTechnology<?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().stop();
            }
        } catch (AccessDeniedException cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void destroy() throws LifecycleException {
        try {
            for (EndpointTechnology<?> endpointTechnology : endpointTechnologies.values()) {
                endpointTechnology.getManager().destroy();
            }
        } catch (AccessDeniedException cause) {
            throw new LifecycleException(cause);
        }
    }

}
