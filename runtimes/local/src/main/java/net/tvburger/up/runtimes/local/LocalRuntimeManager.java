package net.tvburger.up.runtimes.local;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpRuntimeInfo;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.behaviors.impl.SpecificationImpl;
import net.tvburger.up.infra.UpEngineDefinition;
import net.tvburger.up.infra.impl.UpEngineImpl;
import net.tvburger.up.infra.impl.UpEngineManagerImpl;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.runtime.impl.UpContextImpl;
import net.tvburger.up.runtime.impl.UpRuntimeInfoImpl;
import net.tvburger.up.runtime.util.UpContextHolder;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class LocalRuntimeManager extends LifecycleManagerImpl implements UpRuntime.Manager {

    private static final Logger logger = LoggerFactory.getLogger(LocalRuntimeManager.class);

    public static final class Factory {

        public static LocalRuntimeManager create(UpEngineDefinition engineDefinition) {
            Identity identity = Identities.ANONYMOUS;
            UpRuntimeInfo info = UpRuntimeInfoImpl.Factory.create(identity, SpecificationImpl.Factory.create("Up", "0.1.0"));
            LocalRuntimeManager manager = new LocalRuntimeManager(info, identity, engineDefinition);
            return manager;
        }

        private Factory() {
        }

    }

    private final UpRuntimeInfo info;
    private final Identity identity;
    private final UpEngineDefinition engineDefinition;
    private final Map<String, UpEnvironment> environments = new ConcurrentHashMap<>();
    private UpRuntime runtime;
    private UpEngineManagerImpl engineManager;

    private LocalRuntimeManager(UpRuntimeInfo info, Identity identity, UpEngineDefinition engineDefinition) {
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
    public UpEnvironment createEnvironment(String environmentName) throws UpRuntimeException {
        UpContext context = UpContextHolder.getContext();
        try {
            UpContextHolder.setContext(UpContextImpl.Factory.createEngineContext(engineManager.getEngine(), identity));
            logger.info("Adding environment: " + environmentName);
            UpEnvironment environment = LocalEnvironment.Factory.create(getEngine(), environmentName, runtime.getInfo());
            environments.put(environment.getInfo().getName(), environment);
            environment.getManager().init();
            logger.info("Added environment: " + environmentName);
            return environment;
        } catch (LifecycleException | AccessDeniedException cause) {
            logger.error("Failed to add environment: " + cause.getMessage(), cause);
            throw new UpRuntimeException(cause);
        } finally {
            UpContextHolder.setContext(context);
        }
    }

    @Override
    public void init() throws LifecycleException {
        UpContext context = UpContextHolder.getContext();
        super.init();
        try {
            Set<UpEngine.Info> engineInfos = new HashSet<>();
            runtime = LocalRuntimeImpl.Factory.create(this, engineInfos, environments);
            engineManager = UpEngineManagerImpl.Factory.create(new LocalEngineInfo(identity), engineDefinition, identity, runtime);
            engineManager.setEngine(new UpEngineImpl(engineManager));
            UpContextHolder.setContext(UpContextImpl.Factory.createEngineContext(engineManager.getEngine(), identity));
            logger.info("Intializing: " + getInfo());
            engineInfos.add(engineManager.getInfo());
            engineManager.init();
            logger.info("Initialized: " + getInfo());
        } catch (UnknownHostException cause) {
            logger.error("Failed to initialize: " + cause.getMessage(), cause);
            throw new LifecycleException(cause);
        } finally {
            UpContextHolder.setContext(context);
        }
    }

    @Override
    public void start() throws LifecycleException {
        UpContext context = UpContextHolder.getContext();
        UpContextHolder.setContext(UpContextImpl.Factory.createEngineContext(engineManager.getEngine(), identity));
        super.start();
        try {
            logger.info("Starting: " + getInfo());
            for (UpEnvironment environment : environments.values()) {
                environment.getManager().start();
            }
            engineManager.start();
            logger.info("Started: " + getInfo());
        } catch (AccessDeniedException cause) {
            logger.error("Failed to start: " + cause.getMessage(), cause);
            throw new LifecycleException(cause);
        } finally {
            UpContextHolder.setContext(context);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        UpContext context = UpContextHolder.getContext();
        UpContextHolder.setContext(UpContextImpl.Factory.createEngineContext(engineManager.getEngine(), identity));
        super.stop();
        try {
            logger.info("Stopping: " + getInfo());
            for (UpEnvironment environment : environments.values()) {
                UpEnvironment.Manager manager = environment.getManager();
                if (manager.getState() == State.ACTIVE) {
                    environment.getManager().stop();
                }
            }
            engineManager.stop();
            logger.info("Stopped: " + getInfo());
        } catch (AccessDeniedException cause) {
            logger.error("Failed to stop: " + cause.getMessage(), cause);
            throw new LifecycleException(cause);
        } finally {
            UpContextHolder.setContext(context);
        }
    }

    @Override
    public void destroy() throws LifecycleException {
        UpContext context = UpContextHolder.getContext();
        UpContextHolder.setContext(UpContextImpl.Factory.createEngineContext(engineManager.getEngine(), identity));
        super.destroy();
        try {
            logger.info("Destroying: " + getInfo());
            for (UpEnvironment environment : environments.values()) {
                UpEnvironment.Manager manager = environment.getManager();
                switch (manager.getState()) {
                    case ACTIVE:
                        throw new LifecycleException("UpEnvironment is running: " + environment.getInfo().getName());
                    case READY:
                        manager.destroy();
                    default:
                        break;
                }
            }
            engineManager.destroy();
            logger.info("Destroyed: " + getInfo());
        } catch (AccessDeniedException cause) {
            logger.error("Failed to cleanUp: " + cause.getMessage(), cause);
            throw new LifecycleException(cause);
        } finally {
            UpContextHolder.setContext(context);
        }
    }

    void removeEnvironment(String environmentName) {
        logger.info("Removing environment: " + environmentName);
        environments.remove(environmentName);
    }

}
