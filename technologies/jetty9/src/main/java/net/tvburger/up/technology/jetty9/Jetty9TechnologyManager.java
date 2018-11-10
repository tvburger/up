package net.tvburger.up.technology.jetty9;

import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.EndpointTechnologyManager;
import net.tvburger.up.Environment;
import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.impl.LifecycleManagerImpl;
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
public final class Jetty9TechnologyManager extends LifecycleManagerImpl implements EndpointTechnologyManager {

    private final Map<EnvironmentInfo, Set<Jsr340.Endpoint>> endpoints = new HashMap<>();
    private final Map<Jsr340.Endpoint.Info, Jsr340.Endpoint.Definition> endpointDefinitions = new HashMap<>();
    private final Map<Jsr340.Endpoint.Info, Jsr340.Endpoint> infoEndpointMapping = new HashMap<>();
    private final Map<Jsr340.Endpoint, ServletHolder> endpointHolderMapping = new HashMap<>();

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
    public synchronized void init() throws LifecycleException {
        super.init();
        server = new Server();
        http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(0);
        http.setIdleTimeout(30_000);
        server.addConnector(http);
        server.setStopAtShutdown(true);
    }

    @Override
    public synchronized void start() throws LifecycleException {
        super.start();
        doStart();
    }

    private void doStart() throws LifecycleException {
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

    private ContextHandlerCollection createHandler() throws AccessDeniedException, DeployException {
        ArrayList<ServletContextHandler> handlers = new ArrayList<>();
        for (Map.Entry<EnvironmentInfo, Set<Jsr340.Endpoint>> entry : endpoints.entrySet()) {
            ServletContextHandler handler = createServletContextHandler(entry.getKey());
            for (Jsr340.Endpoint endpoint : entry.getValue()) {
                if (endpoint.getManager().getState() == State.ACTIVE) {
                    addEndpointToHandler(handler, endpoint);
                }
            }
            handlers.add(handler);
        }
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(handlers.toArray(new ServletContextHandler[0]));
        return contexts;
    }

    private void addEndpointToHandler(ServletContextHandler handler, Jsr340.Endpoint endpoint) throws AccessDeniedException, DeployException {
        Jsr340.Endpoint.Definition definition = endpointDefinitions.get(endpoint.getInfo());
        ServletHolder holder;
        if (!definition.getInstanceDefinition().getArguments().isEmpty()) {
            Environment environment = getEnvironment(endpoint);
            Object[] arguments = new ArrayList<>(definition.getArguments()).toArray();
            Servlet servlet = Services.instantiateService(environment, definition.getServletClass(), arguments);
            holder = new ServletHolder(new Jetty9ContextServlet(engine, identity, servlet));
        } else {
            holder = handler.addServlet(definition.getServletClass(), definition.getMapping());
        }
        holder.getRegistration().setInitParameters(definition.getInitParameters());
        endpointHolderMapping.put(endpoint, holder);
        handler.addServlet(holder, definition.getMapping());
    }

    private Environment getEnvironment(Jsr340.Endpoint endpoint) throws AccessDeniedException {
        return engine.getRuntime().getEnvironment(endpoint.getInfo().getEnvironmentInfo().getName());
    }

    @Override
    public synchronized void stop() throws LifecycleException {
        super.stop();
        doStop();
    }

    public void doStop() throws LifecycleException {
        try {
            server.stop();
        } catch (Exception cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public synchronized void destroy() throws LifecycleException {
        super.destroy();
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

    private ServletContextHandler createServletContextHandler(EnvironmentInfo environmentInfo) {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/" + environmentInfo.getName());
        return handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void deploy(EnvironmentInfo environmentInfo, EndpointDefinition endpointDefinition) throws
            DeployException, AccessDeniedException {
        try {
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
            String mapping = settings.getOrDefault("mapping", "/");
            Jsr340.Endpoint.Info info = createInfo(environmentInfo, servletClass, mapping);
            endpointDefinitions.put(info, Jsr340.Endpoint.Definition.parse(endpointDefinition));
            Jetty9Endpoint endpoint = Jetty9Endpoint.Factory.create(info, this);
            infoEndpointMapping.put(info, endpoint);
            endpoints.computeIfAbsent(info.getEnvironmentInfo(), k -> new HashSet<>()).add(endpoint);
            endpoint.getManager().init();
            endpoint.getManager().start();
            restartIfNeeded();
        } catch (LifecycleException cause) {
            throw new DeployException(cause);
        }
    }

    public synchronized void restartIfNeeded() throws LifecycleException {
        if (getState() == State.ACTIVE) {
            doStop();
            doStart();
        }
    }

    private Jsr340.Endpoint.Info createInfo(EnvironmentInfo environmentInfo, Class<? extends Servlet> servletClass, String mapping) {
        String serverName = http.getHost();
        String contextPath = "/" + environmentInfo.getName();
        String name = servletClass.getName();
        String url = "http://" + serverName + (port != 80 ? ":" + port : "") + contextPath + mapping;
        return new Jsr340.Endpoint.Info(url, servletClass, port, serverName, contextPath, mapping, name, environmentInfo);
    }

    void destroy(Jsr340.Endpoint.Info info) throws LifecycleException {
        try {
            Jsr340.Endpoint endpoint = infoEndpointMapping.remove(info);
            endpoints.get(info.getEnvironmentInfo()).remove(endpoint);
            endpointDefinitions.remove(info);
            ServletHolder servletHolder = endpointHolderMapping.remove(endpoint);
            servletHolder.destroyInstance(servletHolder.getServletInstance());
        } catch (Exception cause) {
            throw new LifecycleException("Exception while destroying servlet: " + cause.getMessage(), cause);
        }
    }

    @Override
    public Specification getEngineRequirement() {
        return Java8Specification.get();
    }

}
