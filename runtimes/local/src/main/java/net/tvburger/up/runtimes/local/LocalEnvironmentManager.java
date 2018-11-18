package net.tvburger.up.runtimes.local;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.runtime.impl.UpEnvironmentInfoImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.topology.UpApplicationTopology;
import net.tvburger.up.topology.UpEndpointDefinition;
import net.tvburger.up.topology.UpServiceDefinition;
import net.tvburger.up.util.Identities;
import net.tvburger.up.util.UpClassProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public final class LocalEnvironmentManager extends LifecycleManagerImpl implements UpEnvironment.Manager {

    private static final Logger logger = LoggerFactory.getLogger(LocalEnvironmentManager.class);

    public static final class Factory {

        public static LocalEnvironmentManager create(UpEngine engine, String environmentName, UpRuntimeInfo runtimeInfo) {
            UpEnvironment.Info info = UpEnvironmentInfoImpl.Factory.create(environmentName, runtimeInfo, Identities.ANONYMOUS);
            LocalEnvironmentManager manager = new LocalEnvironmentManager(
                    engine, info,
                    new LocalServicesManager(engine, info));
            return manager;
        }

        private Factory() {
        }

    }

    private final UpEngine engine;
    private final UpEnvironment.Info environmentInfo;
    private final LocalServicesManager localServicesManager;
    private boolean logged;

    public LocalEnvironmentManager(UpEngine engine, UpEnvironment.Info environmentInfo, LocalServicesManager localServicesManager) {
        this.engine = engine;
        this.environmentInfo = environmentInfo;
        this.localServicesManager = localServicesManager;
    }

    public LocalServicesManager getLocalServicesManager() {
        return localServicesManager;
    }

    @Override
    public UpEnvironment.Info getInfo() {
        return environmentInfo;
    }

    @Override
    public synchronized void start() throws LifecycleException {
        super.start();
        try {
            UpEnvironment environment = getEnvironment();
            logger.info("Starting...");
            for (UpService.Info<?> serviceInfo : environment.listServices()) {
                environment.getServiceManager(serviceInfo).start();
            }
            for (Map.Entry<Specification, Set<? extends UpEndpoint.Info>> entry : environment.listEndpoints().entrySet()) {
                for (UpEndpoint.Info endpointInfo : entry.getValue()) {
                    environment.getEndpointManager(endpointInfo).start();
                }
            }
            logger.info("Started");
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
            UpEnvironment environment = getEnvironment();
            logger.info("Stopping...");
            for (UpService.Info<?> serviceInfo : environment.listServices()) {
                environment.getServiceManager(serviceInfo).stop();
            }
            for (Map.Entry<Specification, Set<? extends UpEndpoint.Info>> entry : environment.listEndpoints().entrySet()) {
                for (UpEndpoint.Info endpointInfo : entry.getValue()) {
                    environment.getEndpointManager(endpointInfo).stop();
                }
            }
            logger.info("Stopped");
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
            UpEnvironment environment = getEnvironment();
            logger.info("Destroying...");
            for (UpService.Info<?> serviceInfo : environment.listServices()) {
                environment.getServiceManager(serviceInfo).destroy();
            }
            for (Map.Entry<Specification, Set<? extends UpEndpoint.Info>> entry : environment.listEndpoints().entrySet()) {
                for (UpEndpoint.Info endpointInfo : entry.getValue()) {
                    environment.getEndpointManager(endpointInfo).destroy();
                }
            }
            ((LocalRuntimeManager) engine.getRuntime().getManager()).removeEnvironment(environment.getInfo().getName());
            logger.info("Destroyed");
        } catch (LifecycleException | AccessDeniedException cause) {
            logger.error("Failed to start: " + cause.getMessage(), cause);
            fail();
            throw new LifecycleException(cause);
        }
    }

    private UpEnvironment getEnvironment() throws AccessDeniedException {
        return engine.getRuntime().getEnvironment(getInfo().getName());
    }

    @Override
    public UpApplicationTopology dump() {
        throw new UnsupportedOperationException();
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
    public void deploy(UpApplicationTopology applicationTopology) throws TopologyException {
        try {
            logger.info("Deploying application...");
            for (UpServiceDefinition serviceDefinition : applicationTopology.getServiceDefinitions()) {
                deploy(serviceDefinition);
            }
            for (UpEndpointDefinition endpointDefinition : applicationTopology.getEndpointDefinitions()) {
                deploy(endpointDefinition);
            }
            logger.info("Application deployed");
        } catch (TopologyException cause) {
            String message = "Failed to deploy application: " + cause.getMessage();
            logger.error(message, cause);
            throw new TopologyException(message, cause);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deploy(UpServiceDefinition serviceDefinition) throws TopologyException {
        try {
            logger.info("Deploying service: " + serviceDefinition.getServiceType());
            UpService<?> service = getLocalServicesManager().addService(
                    (Class) serviceDefinition.getServiceType(),
                    UpClassProvider.getClass(serviceDefinition.getInstanceDefinition().getInstanceSpecification()),
                    new ArrayList<>(serviceDefinition.getInstanceDefinition().getArguments()).toArray());
            service.getManager().init();
            logger.info("UpService deployed: " + service.getInfo());
        } catch (LifecycleException | AccessDeniedException cause) {
            String message = "Failed to deploy service: " + cause.getMessage();
            logger.error(message, cause);
            throw new TopologyException(message, cause);
        }
    }

    @Override
    public void deploy(UpEndpointDefinition endpointDefinition) throws TopologyException {
        try {
            logger.info("Deploying endpoint: " + endpointDefinition.getEndpointTechnology());
            UpEndpointTechnologyInfo info = getEndpointTechnologyInfo(endpointDefinition.getEndpointTechnology());
            engine.getEndpointTechnology(info.getEndpointType()).getManager().deploy(getInfo(), endpointDefinition);
            logger.info("Deployed endpoint: " + info);
        } catch (AccessDeniedException | TopologyException | UpRuntimeException cause) {
            String message = "Failed to deploy endpoint: " + cause.getMessage();
            logger.error(message, cause);
            throw new TopologyException(message, cause);
        }
    }


    private UpEndpointTechnologyInfo getEndpointTechnologyInfo(Specification endpointReference) throws TopologyException {
        for (Class<?> endpointType : engine.listEndpointTypes()) {
            UpEndpointTechnology<?, ?> endpointTechnology = engine.getEndpointTechnology(endpointType);
            UpEndpointTechnologyInfo info = endpointTechnology.getInfo();
            if (info.getSpecificationName().equals(endpointReference.getSpecificationName())
                    && info.getSpecificationVersion().equals(endpointReference.getSpecificationVersion())) {
                return info;
            }
        }
        throw new TopologyException("No such endpoint technology: " + endpointReference);
    }

}