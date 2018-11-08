package net.tvburger.up.local.impl;

import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.Environment;
import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.EnvironmentManager;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.definitions.EndpointDefinition;
import net.tvburger.up.definitions.ServiceDefinition;
import net.tvburger.up.definitions.UpDeploymentDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.deploy.UpRuntimeInfo;
import net.tvburger.up.impl.EnvironmentInfoImpl;
import net.tvburger.up.logger.UpLogger;
import net.tvburger.up.logger.impl.ConsoleLogger;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.util.Identities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public final class LocalEnvironmentManager implements EnvironmentManager {

    public static final class Factory {

        private static final UpLogger logger = new ConsoleLogger(LocalUpInstance.class.getName());

        public static LocalEnvironmentManager create(UpEngine engine, String environmentName, UpRuntimeInfo runtimeInfo) {
            EnvironmentInfo info = EnvironmentInfoImpl.Factory.create(environmentName, runtimeInfo, Identities.ANONYMOUS);
            LocalEnvironmentManager manager = new LocalEnvironmentManager(
                    engine, info,
                    new LocalServicesManager(engine, info, logger));
            manager.init();
            return manager;
        }

        private Factory() {
        }

    }

    private final UpEngine engine;
    private final EnvironmentInfo environmentInfo;
    private final LocalServicesManager localServicesManager;
    private Environment environment;
    private boolean logged;

    public LocalEnvironmentManager(UpEngine engine, EnvironmentInfo environmentInfo, LocalServicesManager localServicesManager) {
        this.engine = engine;
        this.environmentInfo = environmentInfo;
        this.localServicesManager = localServicesManager;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public LocalServicesManager getLocalServicesManager() {
        return localServicesManager;
    }

    @Override
    public EnvironmentInfo getInfo() {
        return environmentInfo;
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
    public void init() {
        environment = LocalEnvironment.Factory.create(engine, this);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deploy(UpDeploymentDefinition deploymentDefinition) throws AccessDeniedException, DeployException {
        for (ServiceDefinition serviceDefinition : deploymentDefinition.getServiceDefinitions()) {
            deploy(serviceDefinition);
        }
        for (EndpointDefinition endpointDefinition : deploymentDefinition.getEndpointDefinitions()) {
            deploy(endpointDefinition);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deploy(ServiceDefinition serviceDefinition) throws AccessDeniedException, DeployException {
        getLocalServicesManager().addService(
                (Class) serviceDefinition.getServiceType(),
                (Class) serviceDefinition.getInstanceDefinition().getInstanceClass(),
                new ArrayList<>(serviceDefinition.getInstanceDefinition().getArguments()).toArray());

    }

    @Override
    public void deploy(EndpointDefinition endpointDefinition) throws AccessDeniedException, DeployException {
        EndpointTechnologyInfo<?> info = getEndpointReference(endpointDefinition.getEndpointTechnology());
        engine.getEndpointTechnology(info).getManager().deploy(getInfo(), endpointDefinition);
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