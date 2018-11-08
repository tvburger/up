package net.tvburger.up.technology.jetty9;

import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.EndpointTechnologyManager;
import net.tvburger.up.Environment;
import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.definitions.EndpointDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.technology.jsr340.Jsr340;
import net.tvburger.up.util.Services;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.util.*;

// https://www.eclipse.org/jetty/documentation/9.4.x/embedded-examples.html
// TODO: add logging
// TODO: add removal of servlet
// TODO: handle environments (based on host headers)
public final class Jetty9TechnologyManager implements EndpointTechnologyManager {

    private final Map<EnvironmentInfo, Set<Jsr340.Endpoint>> endpoints = new HashMap<>();
    private final Map<EnvironmentInfo, ServletContext> servletContexts = new HashMap<>();
    private final UpEngine engine;
    private final Identity identity;
    private Server server;
    private ServletHandler servletHandler;
    private boolean logged;

    public Jetty9TechnologyManager(UpEngine engine, Identity identity) {
        this.engine = engine;
        this.identity = identity;
    }

    public Set<Jsr340.Endpoint> getEndpoints(EnvironmentInfo environmentInfo) {
        Set<Jsr340.Endpoint> endpoints = this.endpoints.get(environmentInfo);
        return endpoints == null ? Collections.emptySet() : Collections.unmodifiableSet(endpoints);
    }

    public UpEngine getEngine() {
        return engine;
    }

    @Override
    public void init() {
        servletHandler = new ServletHandler();
        server = new Server(8091);
        server.setStopAtShutdown(true);
        server.setHandler(servletHandler);
    }

    @Override
    public void start() throws LifecycleException {
        try {
            server.start();
        } catch (Exception cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        try {
            server.stop();
        } catch (Exception cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void destroy() {
        server.destroy();
        server = null;
        servletHandler = null;
    }

    @Override
    public EndpointTechnologyInfo<Jsr340.Endpoint> getInfo() {
        return Jsr340.TechnologyInfo.get();
    }

    @Override
    public String getImplementationName() {
        return Jetty9Implementation.get().getImplementationName();
    }

    @Override
    public String getImplementationVersion() {
        return Jetty9Implementation.get().getImplementationVersion();
    }

    @Override
    public Specification getSpecification() {
        return Jetty9Implementation.get().getSpecification();
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
//        ServletContext servletContext = servletContexts.computeIfAbsent(environmentInfo, null);
        if (environmentInfo == null || endpointDefinition == null) {
            throw new IllegalArgumentException();
        }
        if (!endpointDefinition.getEndpointTechnology().equals(getSpecification())) {
            throw new DeployException("Unsupported specification!");
        }
        Class<?> serviceClass = endpointDefinition.getInstanceDefinition().getInstanceClass();
        if (!Servlet.class.isAssignableFrom(serviceClass)) {
            throw new DeployException("Illegal service class, not a Servlet: " + serviceClass.getName());
        }
        Map<String, String> settings = endpointDefinition.getSettings();
        if (!settings.containsKey("mapping")) {
            throw new DeployException("Invalid endpoint definition: no mapping specified in settings!");
        }
        Environment environment = engine.getRuntime().getEnvironment(environmentInfo.getName());
        Servlet servlet = Services.instantiateService(environment, (Class<? extends Servlet>) serviceClass, new ArrayList<>(endpointDefinition.getInstanceDefinition().getArguments()).toArray());
        String mapping = settings.get("mapping");
        String envMapping = "/" + environmentInfo.getName() + (mapping.startsWith("/") ? "" : "/") + mapping;
        servletHandler.addServletWithMapping(new ServletHolder(new Jetty9ContextServlet(engine, identity, servlet)), envMapping);
//        servletContext.addServlet("", servlet);
        endpoints.computeIfAbsent(environmentInfo, k -> new HashSet<>()).add(Jetty9Endpoint.Factory.create(servlet));
    }

}
