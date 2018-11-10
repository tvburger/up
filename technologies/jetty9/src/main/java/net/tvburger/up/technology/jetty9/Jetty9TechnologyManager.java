package net.tvburger.up.technology.jetty9;

import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.EndpointTechnologyManager;
import net.tvburger.up.Environment;
import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.technology.jsr340.Jsr340;
import net.tvburger.up.topology.EndpointDefinition;
import net.tvburger.up.util.Java8Specification;
import net.tvburger.up.util.Services;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.util.*;

// https://www.eclipse.org/jetty/documentation/9.4.x/embedded-examples.html
// TODO: add logging
// TODO: add removal of servlet
public final class Jetty9TechnologyManager implements EndpointTechnologyManager {

    private final Map<EnvironmentInfo, Set<Jsr340.Endpoint>> endpoints = new HashMap<>();
    private final Map<EnvironmentInfo, ServletContextHandler> servletContextHandlers = new HashMap<>();
    private final UpEngine engine;
    private final Identity identity;
    private Server server;
    private ServerConnector http;
    private boolean logged;
    private int port;

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
        server = new Server();
        http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(0);
        http.setIdleTimeout(30_000);
        server.addConnector(http);
        server.setStopAtShutdown(true);
    }

    private ContextHandlerCollection createHandler() {
        ServletContextHandler[] handlers = new ServletContextHandler[servletContextHandlers.size()];
        int i = 0;
        for (ServletContextHandler servletContextHandler : servletContextHandlers.values()) {
            handlers[i++] = servletContextHandler;
        }
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(handlers);
        return contexts;
    }

    @Override
    public void start() throws LifecycleException {
        try {
            if (server.isStarting() || server.isStarted()) {
                return;
            }
            if (port != 0) {
                http.setPort(port);
            }
            server.setHandler(createHandler());
            server.start();
            port = http.getLocalPort();
        } catch (Exception cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        try {
            if (server.isStopping() || server.isStopped()) {
                return;
            }
            server.stop();
        } catch (Exception cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void destroy() {
        server.destroy();
        server = null;
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

    private ServletContextHandler create(EnvironmentInfo environmentInfo) {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/" + environmentInfo.getName());
        return handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deploy(EnvironmentInfo environmentInfo, EndpointDefinition endpointDefinition) throws DeployException, AccessDeniedException {
        try {
            ServletContextHandler servletContextHandler = servletContextHandlers.computeIfAbsent(environmentInfo, this::create);
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
            Class<? extends Servlet> servletClass = (Class<? extends Servlet>) serviceClass;
            Map<String, String> settings = endpointDefinition.getSettings();
            if (!settings.containsKey("mapping")) {
                throw new DeployException("Invalid endpoint definition: no mapping specified in settings!");
            }
            Environment environment = engine.getRuntime().getEnvironment(environmentInfo.getName());
            String mapping = settings.getOrDefault("mapping", "/");
            if (endpointDefinition.getInstanceDefinition().getArguments().isEmpty()) {
                servletContextHandler.addServlet(servletClass, mapping);
            } else {
                Servlet servlet = Services.instantiateService(environment, (Class<? extends Servlet>) servletClass, new ArrayList<>(endpointDefinition.getInstanceDefinition().getArguments()).toArray());
                servletContextHandler.addServlet(new ServletHolder(new Jetty9ContextServlet(engine, identity, servlet)), mapping);
            }
            endpoints.computeIfAbsent(environmentInfo, k -> new HashSet<>()).add(Jetty9Endpoint.Factory.create(servletClass, servletContextHandler, http, mapping));
            if (server.isStarted()) {
                stop();
                start();
            }
        } catch (LifecycleException cause) {
            throw new DeployException(cause);
        }
    }

    @Override
    public Specification getEngineRequirement() {
        return Java8Specification.get();
    }

}
