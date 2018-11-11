package net.tvburger.up.technology.jersey2;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.impl.LifecycleManagerImpl;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.technology.jsr340.Jsr340;
import net.tvburger.up.technology.jsr370.Jsr370;
import net.tvburger.up.topology.EndpointDefinition;
import net.tvburger.up.util.Java8Specification;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;

public final class Jersey2TechnologyManager extends LifecycleManagerImpl implements EndpointTechnologyManager<Jsr370.Endpoint> {

    private final UpEngine engine;
    private final Identity identity;
    private boolean logged;

    private final Map<EnvironmentInfo, ServletContainer> servletContainers = new HashMap<>();
    private final Map<EnvironmentInfo, ResourceConfig> resourceConfig = new HashMap<>();
    private EndpointTechnology<Jsr340.Endpoint> servletTechnology;

    public Jersey2TechnologyManager(UpEngine engine, Identity identity) {
        this.engine = engine;
        this.identity = identity;
    }

    public UpEngine getEngine() {
        return engine;
    }

    @Override
    public synchronized void init() throws LifecycleException {
        try {
            super.init();
            servletTechnology = engine.getEndpointTechnology(Jsr340.Endpoint.class);
        } catch (DeployException | AccessDeniedException cause) {
            fail();
            throw new LifecycleException(cause);
        }
    }

    @Override
    public synchronized void start() throws LifecycleException {

    }

    @Override
    public synchronized void stop() throws LifecycleException {

    }

    @Override
    public synchronized void destroy() throws LifecycleException {

    }

    @Override
    public EndpointTechnologyInfo<Jsr370.Endpoint> getInfo() {
        return Jsr370.TechnologyInfo.get();
    }

    @Override
    public String getImplementationName() {
        return Jersey2Implementation.get().getImplementationName();
    }

    @Override
    public String getImplementationVersion() {
        return Jersey2Implementation.get().getImplementationVersion();
    }

    @Override
    public Specification getSpecification() {
        return Jersey2Implementation.get().getSpecification();
    }

    @Override
    public boolean isLogged() {
        return logged;
    }

    @Override
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deploy(EnvironmentInfo environmentInfo, EndpointDefinition endpointDefinition) throws DeployException, AccessDeniedException {
        Class<?> instanceClass = endpointDefinition.getInstanceDefinition().getInstanceClass();
        if (!Application.class.isAssignableFrom(instanceClass)) {
            throw new DeployException("Invalid application class: " + instanceClass);
        }
        Class<?> applicationClass = instanceClass;
        Environment environment = engine.getRuntime().getEnvironment(environmentInfo.getName());
//        Application application = Services.instantiateService(environment, applicationClass, endpointDefinition.getInstanceDefinition().getArguments());
//        Endpoint endpoint = RuntimeDelegate.getInstance().createEndpoint(application, Endpoint.class); // implementation specific
    }

    @Override
    public Specification getEngineRequirement() {
        return Java8Specification.get();
    }

}
