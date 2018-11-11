package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.impl.EnvironmentInfoImpl;
import net.tvburger.up.impl.LifecycleManagerImpl;
import net.tvburger.up.logger.UpLogger;
import net.tvburger.up.loggers.console.ConsoleLogger;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntimeInfo;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.topology.EndpointDefinition;
import net.tvburger.up.topology.ServiceDefinition;
import net.tvburger.up.topology.UpApplicationTopology;
import net.tvburger.up.util.Environments;
import net.tvburger.up.util.Identities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public final class LocalEnvironmentManager extends LifecycleManagerImpl implements EnvironmentManager {

    private static final Logger logger = LoggerFactory.getLogger(LocalEnvironmentManager.class);

    public static final class Factory {

        private static final UpLogger logger = new ConsoleLogger(LocalUpInstance.class.getName());

        public static LocalEnvironmentManager create(UpEngine engine, String environmentName, UpRuntimeInfo runtimeInfo) {
            EnvironmentInfo info = EnvironmentInfoImpl.Factory.create(environmentName, runtimeInfo, Identities.ANONYMOUS);
            LocalEnvironmentManager manager = new LocalEnvironmentManager(
                    engine, info,
                    new LocalServicesManager(engine, info, logger));
            return manager;
        }

        private Factory() {
        }

    }

    private final UpEngine engine;
    private final EnvironmentInfo environmentInfo;
    private final LocalServicesManager localServicesManager;
    private boolean logged;

    public LocalEnvironmentManager(UpEngine engine, EnvironmentInfo environmentInfo, LocalServicesManager localServicesManager) {
        this.engine = engine;
        this.environmentInfo = environmentInfo;
        this.localServicesManager = localServicesManager;
    }

    public LocalServicesManager getLocalServicesManager() {
        return localServicesManager;
    }

    @Override
    public EnvironmentInfo getInfo() {
        return environmentInfo;
    }

    @Override
    public synchronized void start() throws LifecycleException {
        super.start();
        try {
            Environment environment = getEnvironment();
            logger.info("Starting...");
            for (Service<?> service : environment.getServices()) {
                service.getManager().start();
            }
            for (Endpoint<?, ?> endpoint : Environments.getEndpoints(environment)) {
                endpoint.getManager().start();
            }
            logger.info("Started");
        } catch (LifecycleException | AccessDeniedException | DeployException cause) {
            logger.error("Failed to start: " + cause.getMessage(), cause);
            fail();
            throw new LifecycleException(cause);
        }
    }

    @Override
    public synchronized void stop() throws LifecycleException {
        super.stop();
        try {
            Environment environment = getEnvironment();
            logger.info("Stopping...");
            for (Service<?> service : environment.getServices()) {
                service.getManager().stop();
            }
            for (Endpoint<?, ?> endpoint : Environments.getEndpoints(environment)) {
                endpoint.getManager().stop();
            }
            logger.info("Stopped");
        } catch (LifecycleException | AccessDeniedException | DeployException cause) {
            logger.error("Failed to stop: " + cause.getMessage(), cause);
            fail();
            throw new LifecycleException(cause);
        }
    }

    @Override
    public synchronized void destroy() throws LifecycleException {
        super.destroy();
        try {
            Environment environment = getEnvironment();
            logger.info("Destroying...");
            for (Service<?> service : environment.getServices()) {
                service.getManager().destroy();
            }
            for (Endpoint<?, ?> endpoint : Environments.getEndpoints(environment)) {
                endpoint.getManager().destroy();
            }
            ((LocalUpRuntimeManager) engine.getRuntime().getManager()).removeEnvironment(environment.getInfo().getName());
            logger.info("Destroyed");
        } catch (LifecycleException | AccessDeniedException | DeployException cause) {
            logger.error("Failed to start: " + cause.getMessage(), cause);
            fail();
            throw new LifecycleException(cause);
        }
    }

    private Environment getEnvironment() throws AccessDeniedException {
        return engine.getRuntime().getEnvironment(getInfo().getName());
    }

    @Override
    public void dump(OutputStream out) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void restore(InputStream in) throws IOException {
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
    public void deploy(UpApplicationTopology deploymentDefinition) throws DeployException {
        try {
            logger.info("Deploying application...");
            for (ServiceDefinition serviceDefinition : deploymentDefinition.getServiceDefinitions()) {
                deploy(serviceDefinition);
            }
            for (EndpointDefinition endpointDefinition : deploymentDefinition.getEndpointDefinitions()) {
                deploy(endpointDefinition);
            }
            logger.info("Application deployed");
        } catch (DeployException cause) {
            String message = "Failed to deploy application: " + cause.getMessage();
            logger.error(message, cause);
            throw new DeployException(message, cause);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deploy(ServiceDefinition serviceDefinition) throws DeployException {
        try {
            logger.info("Deploying service: " + serviceDefinition.getServiceType());
            Service<?> service = getLocalServicesManager().addService(
                    (Class) serviceDefinition.getServiceType(),
                    (Class) serviceDefinition.getInstanceDefinition().getInstanceClass(),
                    new ArrayList<>(serviceDefinition.getInstanceDefinition().getArguments()).toArray());
            service.getManager().init();
            logger.info("Service deployed: " + service.getInfo());
        } catch (LifecycleException | AccessDeniedException cause) {
            String message = "Failed to deploy service: " + cause.getMessage();
            logger.error(message, cause);
            throw new DeployException(message, cause);
        }
    }

    @Override
    public void deploy(EndpointDefinition endpointDefinition) throws DeployException {
        try {
            logger.info("Deploying endpoint: " + endpointDefinition.getEndpointTechnology());
            EndpointTechnologyInfo<?> info = getEndpointReference(endpointDefinition.getEndpointTechnology());
            engine.getEndpointTechnology(info).getManager().deploy(getInfo(), endpointDefinition);
            logger.info("Deployed endpoint: " + info);
        } catch (AccessDeniedException | DeployException cause) {
            String message = "Failed to deploy endpoint: " + cause.getMessage();
            logger.error(message, cause);
            throw new DeployException(message, cause);
        }
    }


    private EndpointTechnologyInfo<?> getEndpointReference(Specification endpointReference) throws DeployException, AccessDeniedException {
        for (EndpointTechnologyInfo<?> info : engine.getEndpointTechnologies()) {
            if (info.getSpecificationName().equals(endpointReference.getSpecificationName())
                    && info.getSpecificationVersion().equals(endpointReference.getSpecificationVersion())) {
                return info;
            }
        }
        throw new DeployException("No such endpoint: " + endpointReference);
    }

}