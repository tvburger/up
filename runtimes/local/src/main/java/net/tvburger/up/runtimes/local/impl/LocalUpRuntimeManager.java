package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.Environment;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.impl.*;
import net.tvburger.up.runtime.*;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.topology.UpEngineDefinition;
import net.tvburger.up.util.Identities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class LocalUpRuntimeManager extends LifecycleManagerImpl implements UpRuntimeManager {

    private static final Logger logger = LoggerFactory.getLogger(LocalUpRuntimeManager.class);

    public static final class Factory {

        public static LocalUpRuntimeManager create(UpEngineDefinition engineDefinition) {
            Identity identity = Identities.ANONYMOUS;
            UpRuntimeInfo info = UpRuntimeInfoImpl.Factory.create(identity, SpecificationImpl.Factory.create("Up", "0.1.0"));
            LocalUpRuntimeManager manager = new LocalUpRuntimeManager(info, identity, engineDefinition);
            return manager;
        }

        private Factory() {
        }

    }

    private final UpRuntimeInfo info;
    private final Identity identity;
    private final UpEngineDefinition engineDefinition;
    private final Map<String, Environment> environments = new ConcurrentHashMap<>();
    private UpRuntime runtime;
    private UpEngineManagerImpl engineManager;

    private LocalUpRuntimeManager(UpRuntimeInfo info, Identity identity, UpEngineDefinition engineDefinition) {
        this.info = info;
        this.identity = identity;
        this.engineDefinition = engineDefinition;
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
            logger.info("Adding environment: " + environmentName);
            Environment environment = LocalEnvironment.Factory.create(getEngine(), environmentName, runtime.getInfo());
            environments.put(environment.getInfo().getName(), environment);
            environment.getManager().init();
            return environment;
        } catch (LifecycleException cause) {
            logger.error("Failed to add environment: " + cause.getMessage(), cause);
            throw new DeployException(cause);
        }
    }

    @Override
    public void init() throws LifecycleException {
        super.init();
        try {
            logger.info("Intializing...");
            Set<UpEngine> engines = new HashSet<>();
            runtime = UpRuntimeImpl.Factory.create(this, engines, environments);
            engineManager = UpEngineManagerImpl.Factory.create(new LocalUpEngineInfo(identity), engineDefinition, identity, runtime);
            engineManager.setEngine(new UpEngineImpl(engineManager));
            engines.add(engineManager.getEngine());
            engineManager.init();
            logger.info("Initialized");
        } catch (UnknownHostException cause) {
            logger.error("Failed to initialize: " + cause.getMessage(), cause);
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void start() throws LifecycleException {
        super.start();
        try {
            logger.info("Starting...");
            for (Environment environment : environments.values()) {
                environment.getManager().start();
            }
            engineManager.start();
            logger.info("Started");
        } catch (AccessDeniedException cause) {
            logger.error("Failed to start: " + cause.getMessage(), cause);
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        super.stop();
        try {
            logger.info("Stopping...");
            for (Environment environment : environments.values()) {
                System.out.println(environment.getInfo());
                System.out.println(environment.getManager().getState());
                environment.getManager().stop();
                System.out.println(environment.getManager().getState());
            }
            engineManager.stop();
            logger.info("Stopped");
        } catch (AccessDeniedException cause) {
            logger.error("Failed to stop: " + cause.getMessage(), cause);
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void destroy() throws LifecycleException {
        super.destroy();
        try {
            for (Environment environment : environments.values()) {
                environment.getManager().destroy();
            }
            engineManager.destroy();
        } catch (AccessDeniedException cause) {
            logger.error("Failed to destroy: " + cause.getMessage(), cause);
            throw new LifecycleException(cause);
        }
    }

    void removeEnvironment(String environmentName) {
        logger.info("Removing environment: " + environmentName);
        environments.remove(environmentName);
    }

}
