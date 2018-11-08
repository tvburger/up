package net.tvburger.up.local;

import net.tvburger.up.Environment;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.deploy.*;
import net.tvburger.up.impl.UpRuntimeImpl;
import net.tvburger.up.impl.UpRuntimeInfoImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class LocalUpRuntimeManager implements UpRuntimeManager {

    public static final class Factory {

        public static LocalUpRuntimeManager create() throws LifecycleException {
            Identity identity = Identities.ANONYMOUS;
            UpRuntimeInfo info = UpRuntimeInfoImpl.Factory.create(identity, LocalUpEngineManager.getImplementation().getSpecification());
            LocalUpRuntimeManager manager = new LocalUpRuntimeManager(info, identity);
            manager.init();
            return manager;
        }

        private Factory() {
        }

    }

    private final UpRuntimeInfo info;
    private final Identity identity;
    private final Map<String, Environment> environments = new ConcurrentHashMap<>();
    private UpRuntime runtime;
    private LocalUpEngineManager engineManager;

    private LocalUpRuntimeManager(UpRuntimeInfo info, Identity identity) {
        this.info = info;
        this.identity = identity;
    }

    public UpRuntime getRuntime() {
        return runtime;
    }

    public UpEngine getEngine() {
        return engineManager.getEngine();
    }

    @Override
    public UpRuntimeInfo getInfo() {
        return info;
    }

    @Override
    public Set<Environment> getEnvironments() {
        return Collections.unmodifiableSet(new HashSet<>(environments.values()));
    }

    @Override
    public Environment createEnvironment(String environmentName) throws AccessDeniedException, DeployException {
        try {
            Environment environment = LocalEnvironmentManager.Factory.create(getEngine(), environmentName, runtime.getInfo()).getEnvironment();
            environment.getManager().init();
            environment.getManager().start();
            environments.put(environment.getInfo().getName(), environment);
            return environment;
        } catch (LifecycleException cause) {
            throw new DeployException(cause);
        }
    }

    @Override
    public void init() throws LifecycleException {
        try {
            Set<UpEngine> engines = new HashSet<>();
            runtime = UpRuntimeImpl.Factory.create(this, engines, environments);
            engineManager = LocalUpEngineManager.Factory.create(runtime, identity);
            engines.add(engineManager.getEngine());
            engineManager.init();
        } catch (UnknownHostException cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void start() {
        engineManager.start();
    }

    @Override
    public void stop() {
        engineManager.stop();
    }

    @Override
    public void destroy() {
        engineManager.destroy();
    }

}
