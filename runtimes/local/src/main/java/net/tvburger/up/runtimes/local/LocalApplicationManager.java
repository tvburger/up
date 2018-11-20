package net.tvburger.up.runtimes.local;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEndpointDefinition;
import net.tvburger.up.deploy.UpServiceDefinition;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalApplicationManager extends LifecycleManagerImpl implements UpApplication.Manager {

    private static final Logger logger = LoggerFactory.getLogger(LocalApplicationManager.class);

    private final Map<UpService.Info<?>, UpService<?>> services = new ConcurrentHashMap<>();
    private final Map<Specification, Map<UpEndpoint.Info, UpEndpoint.Manager<?>>> endpoints = new ConcurrentHashMap<>();

    private final LocalServicesManager servicesManager;
    private final UpEngine engine;
    private final UpApplication.Info info;

    private UpApplication application;
    private boolean logged = true;

    public LocalApplicationManager(LocalServicesManager servicesManager, UpEngine engine, UpApplication.Info info) {
        this.servicesManager = servicesManager;
        this.engine = engine;
        this.info = info;
    }

    void init(UpApplication application) throws DeployException {
        try {
            this.application = application;
            init();
        } catch (LifecycleException cause) {
            throw new DeployException(cause);
        }
    }

    Map<UpService.Info<?>, UpService<?>> getServices() {
        return services;
    }

    Map<Specification, Map<UpEndpoint.Info, UpEndpoint.Manager<?>>> getEndpoints() {
        return endpoints;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UpService.Manager<?> deployService(UpServiceDefinition serviceDefinition) throws DeployException {
        try {
            logger.info("Deploying service: " + serviceDefinition.getServiceType());
            UpService<?> service = servicesManager.addService(application,
                    (Class) application.getPackage().getClassLoader().loadClass(serviceDefinition.getServiceType()),
                    application.getPackage().getClassLoader().loadClass(serviceDefinition.getInstanceDefinition().getClassSpecification()),
                    new ArrayList<>(serviceDefinition.getInstanceDefinition().getArguments()).toArray());
            services.put(service.getInfo(), service);
            service.getManager().init();
            logger.info("UpService deployed: " + service.getInfo());
            return service.getManager();
        } catch (LifecycleException | ClassNotFoundException | AccessDeniedException cause) {
            String message = "Failed to deploy service: " + cause.getMessage();
            logger.error(message, cause);
            throw new DeployException(message, cause);
        }
    }

    @Override
    public UpEndpoint.Manager<?> deployEndpoint(UpEndpointDefinition endpointDefinition) throws DeployException {
        try {
            UpEndpoint.Manager<?> manager = engine.getEndpointTechnology(endpointDefinition.getEndpointTechnology()).getManager().deployEndpoint(endpointDefinition, application);
            endpoints.computeIfAbsent(endpointDefinition.getEndpointTechnology(), (s) -> new ConcurrentHashMap<>()).put(manager.getInfo(), manager);
            return manager;
        } catch (AccessDeniedException cause) {
            throw new DeployException(cause);
        }
    }

    @Override
    public synchronized void start() throws LifecycleException {
        super.start();
        try {
            logger.info("Starting: " + getInfo());
            for (UpService<?> service : services.values()) {
                service.getManager().start();
            }
            for (Map.Entry<Specification, Map<UpEndpoint.Info, UpEndpoint.Manager<?>>> entry : endpoints.entrySet()) {
                for (UpEndpoint.Manager<?> endpointManager : entry.getValue().values()) {
                    endpointManager.start();
                }
            }
            logger.info("Started: " + getInfo());
        } catch (LifecycleException | AccessDeniedException cause) {
            logger.error("Failed to start: " + cause.getMessage(), cause);
            fail();
            throw new LifecycleException(cause);
        }
    }

    @Override
    public synchronized void stop() throws LifecycleException {
        super.stop();
        try {
            logger.info("Stopping: " + getInfo());
            for (UpService<?> service : services.values()) {
                service.getManager().stop();
            }
            for (Map.Entry<Specification, Map<UpEndpoint.Info, UpEndpoint.Manager<?>>> entry : endpoints.entrySet()) {
                for (UpEndpoint.Manager<?> endpointManager : entry.getValue().values()) {
                    endpointManager.stop();
                }
            }
            logger.info("Stopped: " + getInfo());
        } catch (LifecycleException | AccessDeniedException cause) {
            logger.error("Failed to stop: " + cause.getMessage(), cause);
            fail();
            throw new LifecycleException(cause);
        }
    }

    @Override
    public synchronized void destroy() throws LifecycleException {
        super.destroy();
        try {
            logger.info("Destroying: " + getInfo());
            for (UpService<?> service : services.values()) {
                service.getManager().destroy();
            }
            for (Map.Entry<Specification, Map<UpEndpoint.Info, UpEndpoint.Manager<?>>> entry : endpoints.entrySet()) {
                for (UpEndpoint.Manager<?> endpointManager : entry.getValue().values()) {
                    endpointManager.destroy();
                }
            }
            logger.info("Destroyed: " + getInfo());
        } catch (LifecycleException | AccessDeniedException cause) {
            logger.error("Failed to start: " + cause.getMessage(), cause);
            fail();
            throw new LifecycleException(cause);
        }
    }

    @Override
    public boolean isLogged() {
        return logged;
    }

    @Override
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public UpApplication.Info getInfo() {
        return info;
    }

}
