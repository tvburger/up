package net.tvburger.up.runtimes.local;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.deploy.*;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.impl.UpApplicationInfoImpl;
import net.tvburger.up.runtime.impl.UpEnvironmentInfoImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.util.Identities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private final Map<UpPackage.Info, UpPackage> packages = new HashMap<>();
    private final Map<UpApplication.Info, UpApplication> applications = new HashMap<>();
    private final UpEngine engine;
    private final UpEnvironment.Info environmentInfo;
    private final LocalServicesManager localServicesManager;
    private boolean logged;

    public LocalEnvironmentManager(UpEngine engine, UpEnvironment.Info environmentInfo, LocalServicesManager localServicesManager) {
        this.engine = engine;
        this.environmentInfo = environmentInfo;
        this.localServicesManager = localServicesManager;
    }

    public Map<UpPackage.Info, UpPackage> getPackages() {
        return Collections.unmodifiableMap(packages);
    }

    public Map<UpApplication.Info, UpApplication> getApplications() {
        return applications;
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
            logger.info("Starting: " + getInfo());
            for (UpApplication application : applications.values()) {
                application.getManager().start();
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
            for (UpApplication application : applications.values()) {
                application.getManager().stop();
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
            UpEnvironment environment = getEnvironment();
            logger.info("Destroying: " + getInfo());
            for (UpApplication application : applications.values()) {
                application.getManager().destroy();
            }
            ((LocalRuntimeManager) engine.getRuntime().getManager()).removeEnvironment(environment.getInfo().getName());
            logger.info("Destroyed: " + getInfo());
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
    public boolean isLogged() {
        return logged;
    }

    @Override
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public boolean supportsPackageDefinitionType(Class<? extends UpPackageDefinition> packageDefinitionType) {
        return LocalPackageDefinition.class.equals(packageDefinitionType);
    }

    @Override
    public UpPackage.Manager deployPackage(UpPackageDefinition packageDefinition) throws DeployException {
        if (!supportsPackageDefinitionType(packageDefinition.getClass())) {
            throw new DeployException("Invalid package definition type!");
        }
        try {
            logger.info("Deploying package...");
            LocalPackageDefinition localPackage = (LocalPackageDefinition) packageDefinition;
            packages.put(localPackage.getInfo(), localPackage);
            logger.info("Deployed package: " + localPackage.getInfo());
            return localPackage.getManager();
        } catch (AccessDeniedException cause) {
            throw new DeployException(cause);
        }
    }

    @Override
    public UpApplication.Manager createApplication(String name, UpPackage.Info packageInfo) throws DeployException {
        Objects.requireNonNull(name);
        Objects.requireNonNull(packageInfo);
        logger.info("Creating application: " + name);
        UpPackage upPackage = packages.get(packageInfo);
        if (upPackage == null) {
            throw new DeployException("No such package: " + packageInfo);
        }
        UpApplication.Info info = new UpApplicationInfoImpl(name, packageInfo, environmentInfo, Identities.ANONYMOUS);
        LocalApplicationManager manager = new LocalApplicationManager(localServicesManager, engine, info);
        UpApplication application = LocalApplication.Factory.create(manager, upPackage, Identities.ANONYMOUS);
        manager.init(application);
        applications.put(application.getInfo(), application);
        logger.info("Created application: " + name);
        return manager;
    }

    @Override
    public UpApplication.Manager deployApplication(UpApplicationDefinition applicationDefinition, UpPackage.Info packageInfo) throws DeployException {
        Objects.requireNonNull(applicationDefinition);
        Objects.requireNonNull(packageInfo);
        try {
            logger.info("Deploying application: " + applicationDefinition.getName());
            UpApplication.Manager manager = createApplication(applicationDefinition.getName(), packageInfo);
            for (UpServiceDefinition serviceDefinition : applicationDefinition.getServiceDefinitions()) {
                manager.deployService(serviceDefinition);
            }
            for (UpEndpointDefinition endpointDefinition : applicationDefinition.getEndpointDefinitions()) {
                manager.deployEndpoint(endpointDefinition);
            }
            logger.info("Application deployed: " + applicationDefinition.getName());
            return manager;
        } catch (DeployException cause) {
            String message = "Failed to deploy application: " + cause.getMessage();
            logger.error(message, cause);
            throw new DeployException(message, cause);
        }
    }

    @Override
    public UpService.Manager<?> deployService(UpServiceDefinition serviceDefinition, UpApplication.Info applicationInfo) throws DeployException {
        Objects.requireNonNull(serviceDefinition);
        Objects.requireNonNull(applicationInfo);
        try {
            return applications.get(applicationInfo).getManager().deployService(serviceDefinition);
        } catch (AccessDeniedException cause) {
            throw new DeployException(cause);
        }
    }

    @Override
    public UpEndpoint.Manager<?> deployEndpoint(UpEndpointDefinition endpointDefinition, UpApplication.Info applicationInfo) throws DeployException {
        Objects.requireNonNull(endpointDefinition);
        Objects.requireNonNull(applicationInfo);
        try {
            return applications.get(applicationInfo).getManager().deployEndpoint(endpointDefinition);
        } catch (AccessDeniedException cause) {
            throw new DeployException(cause);
        }
    }

}