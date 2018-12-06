package net.tvburger.up.technology.jetty9;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpPackage;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEndpointDefinition;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.util.UpServices;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;
import net.tvburger.up.technology.jsr340.Jsr340;
import net.tvburger.up.util.Identities;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.Servlet;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

// https://www.eclipse.org/jetty/documentation/9.4.x/embedded-examples.html
// TODO: add logging
public final class Jetty9TechnologyManager extends LifecycleManagerImpl implements Jsr340.Manager {

    private static final Logger logger = LoggerFactory.getLogger(Jetty9TechnologyManager.class);

    private final Map<UpEnvironment.Info, Map<UpApplication.Info, Set<Jetty9Endpoint.Info>>> environments = new HashMap<>();
    private final Map<Jetty9Endpoint.Info, Jetty9Endpoint.Definition> endpointDefinitions = new HashMap<>();
    private final Map<Jetty9Endpoint.Info, UpApplication> endpointApplicationMapping = new HashMap<>();
    private final Map<Jetty9Endpoint.Info, UpPackage> endpointPackageMapping = new HashMap<>();
    private final Map<Jetty9Endpoint.Info, Jetty9Endpoint> infoEndpointMapping = new HashMap<>();
    private final Map<Jetty9Endpoint, ServletHolder> endpointHolderMapping = new HashMap<>();

    private final UpEngine engine;
    private final Identity identity;
    private Server server;
    private ServerConnector http;
    private boolean logged;
    private int port;

    Jetty9TechnologyManager(UpEngine engine, Identity identity) {
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
            server = new Server();
            http = new ServerConnector(server);
            http.setHost(InetAddress.getLocalHost().getHostName());
            http.setPort(0);
            http.setIdleTimeout(30_000);
            server.addConnector(http);
            server.setStopAtShutdown(true);
        } catch (UnknownHostException cause) {
            String message = "Failed to initialize endpoint technology: " + cause.getMessage();
            logger.error(message, cause);
            fail();
            throw new LifecycleException(message, cause);
        }
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
        for (Map.Entry<UpEnvironment.Info, Map<UpApplication.Info, Set<Jsr340.Endpoint.Info>>> entry : environments.entrySet()) {
            ServletContextHandler handler = createServletContextHandler(entry.getKey());
            for (Set<Jsr340.Endpoint.Info> endpointsInfos : entry.getValue().values()) {
                for (Jetty9Endpoint.Info endpointInfo : endpointsInfos) {
                    Jetty9Endpoint endpoint = infoEndpointMapping.get(endpointInfo);
                    if (endpoint.getManager().getState() == State.ACTIVE) {
                        String mapping = addEndpointToHandler(handler, endpoint);
                        handler.addFilter(
                                new FilterHolder(
                                        new Jetty9ContextFilter(
                                                identity,
                                                endpointApplicationMapping.get(endpointInfo),
                                                endpointPackageMapping.get(endpointInfo),
                                                endpoint)),
                                mapping,
                                EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
                    }
                }
            }
            handlers.add(handler);
        }
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(handlers.toArray(new ServletContextHandler[0]));
        return contexts;
    }

    private String addEndpointToHandler(ServletContextHandler handler, Jetty9Endpoint endpoint) throws AccessDeniedException, DeployException {
        try {
            Jetty9Endpoint.Definition definition = endpointDefinitions.get(endpoint.getInfo());
            UpPackage upPackage = endpointPackageMapping.get(endpoint.getInfo());
            ServletHolder holder;
            Class<? extends Servlet> servletClass = upPackage.getClassLoader().loadClass(definition.getServletSpecification(), Servlet.class);
            if (!definition.getInstanceDefinition().getArguments().isEmpty()) {
                UpEnvironment environment = getEnvironment(endpoint);
                Object[] arguments = new ArrayList<>(definition.getArguments()).toArray();
                Servlet servlet = UpServices.instantiateService(environment, servletClass, arguments);
                holder = new ServletHolder(servlet);
                handler.addServlet(holder, definition.getMapping());
            } else {
                holder = handler.addServlet(servletClass, definition.getMapping());
            }
            holder.getRegistration().setInitParameters(definition.getInitParameters());
            endpointHolderMapping.put(endpoint, holder);
            return definition.getMapping();
        } catch (ClassNotFoundException cause) {
            throw new DeployException("Failed to deploy endpoint: " + endpoint.getInfo(), cause);
        }
    }

    private UpEnvironment getEnvironment(Jetty9Endpoint endpoint) throws AccessDeniedException {
        Objects.requireNonNull(endpoint);
        UpEnvironment environment = engine.getRuntime().getEnvironment(endpoint.getInfo().getApplicationInfo().getEnvironmentInfo().getName());
        if (environment == null) {
            throw new NullPointerException("No such environment: " + endpoint.getInfo().getApplicationInfo().getEnvironmentInfo().getName());
        }
        return environment;
    }

    @Override
    public synchronized void stop() throws LifecycleException {
        super.stop();
        doStop();
    }

    private void doStop() throws LifecycleException {
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
    public Jsr340.Info getInfo() {
        return Jsr340.Info.get();
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

    private ServletContextHandler createServletContextHandler(UpEnvironment.Info environmentInfo) {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/" + environmentInfo.getName());
        return handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized Jsr340.Endpoint.Manager deployEndpoint(UpEndpointDefinition endpointDefinition, UpApplication application, UpPackage upPackage) throws DeployException {
        try {
            if (endpointDefinition == null || application == null) {
                throw new IllegalArgumentException();
            }
            logger.info("Deploying endpoint for: " + application.getInfo());
            if (!endpointDefinition.getEndpointTechnology().equals(getSpecification())) {
                throw new DeployException("Unsupported specification!");
            }
            Class<? extends Servlet> servletClass = upPackage.getClassLoader().loadClass(endpointDefinition.getInstanceDefinition().getClassSpecification(), Servlet.class);
            Map<String, String> settings = endpointDefinition.getSettings();
            if (!settings.containsKey("mapping")) {
                throw new DeployException("Invalid endpoint definition: no mapping specified in settings!");
            }
            String mapping = settings.getOrDefault("mapping", "/");
            Jsr340.Endpoint.Info info = createInfo(application.getInfo(), servletClass, mapping);
            endpointDefinitions.put(info, Jsr340.Endpoint.Definition.parse(endpointDefinition));
            Jetty9Endpoint endpoint = Jetty9Endpoint.Factory.create(info, this);
            infoEndpointMapping.put(info, endpoint);
            endpointApplicationMapping.put(info, application);
            endpointPackageMapping.put(info, upPackage);
            environments.computeIfAbsent(info.getApplicationInfo().getEnvironmentInfo(), k -> new ConcurrentHashMap<>())
                    .computeIfAbsent(application.getInfo(), k -> new CopyOnWriteArraySet<>())
                    .add(info);
            endpoint.getManager().init();
            logger.info("UpEndpoint deployed: " + info);
            return endpoint.getManager();
        } catch (LifecycleException | IllegalArgumentException | ClassNotFoundException | AccessDeniedException cause) {
            String message = "Failed to deploy endpoint: " + cause.getMessage();
            logger.error(message, cause);
            throw new DeployException(message, cause);
        }
    }

    private synchronized void restartIfNeeded() throws LifecycleException {
        if (getState() == State.ACTIVE) {
            logger.info("Restarting server!");
            doStop();
            doStart();
        }
    }

    private Jsr340.Endpoint.Info createInfo(UpApplication.Info applicationInfo, Class<? extends Servlet> servletClass, String mapping) {
        String serverName = http.getHost();
        String contextPath = "/" + applicationInfo.getEnvironmentInfo().getName();
        String name = servletClass.getName();
        String url = "http://" + serverName + (port != 80 ? ":" + port : "") + contextPath + mapping;
        Identification identification = Identities.ANONYMOUS;
        return new Jsr340.Endpoint.Info(URI.create(url), identification, servletClass, port, serverName, contextPath, mapping, name, applicationInfo);
    }

    Map<UpApplication.Info, Set<Jsr340.Endpoint.Info>> listEndpoints(UpEnvironment.Info environmentInfo) {
        Map<UpApplication.Info, Set<Jsr340.Endpoint.Info>> endpoints = new HashMap<>();
        for (Map.Entry<UpApplication.Info, Set<Jsr340.Endpoint.Info>> entry : this.environments.get(environmentInfo).entrySet()) {
            endpoints.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
        }
        return endpoints;
    }

    Jsr340.Endpoint.Manager getEndpointManager(Jsr340.Endpoint.Info endpointInfo) throws AccessDeniedException {
        Jsr340.Endpoint endpoint = infoEndpointMapping.get(endpointInfo);
        return endpoint == null ? null : endpoint.getManager();
    }

    synchronized void start(Jsr340.Endpoint.Info info) throws LifecycleException {
        try {
            Jetty9Endpoint endpoint = infoEndpointMapping.get(info);
            endpoint.getManager().doStart();
            restartIfNeeded();
        } catch (Exception cause) {
            logger.error("Failed to start endpoint: " + cause.getMessage(), cause);
            throw new LifecycleException("Exception while starting servlet: " + cause.getMessage(), cause);
        }
    }

    synchronized void stop(Jsr340.Endpoint.Info info) throws LifecycleException {
        try {
            Jetty9Endpoint endpoint = infoEndpointMapping.get(info);
            endpoint.getManager().doStop();
            restartIfNeeded();
        } catch (Exception cause) {
            logger.error("Failed to stop endpoint: " + cause.getMessage(), cause);
            throw new LifecycleException("Exception while stopping servlet: " + cause.getMessage(), cause);
        }
    }

    synchronized void destroy(Jsr340.Endpoint.Info info) throws LifecycleException {
        try {
            logger.info("Removing endpoint: " + info);
            Jetty9Endpoint endpoint = infoEndpointMapping.remove(info);
            endpoint.getManager().doDestroy();
            environments.get(info.getApplicationInfo().getEnvironmentInfo())
                    .get(info.getApplicationInfo())
                    .remove(info);
            endpointDefinitions.remove(info);
            endpointApplicationMapping.remove(info);
            endpointPackageMapping.remove(info);
            ServletHolder servletHolder = endpointHolderMapping.remove(endpoint);
            servletHolder.destroyInstance(servletHolder.getServletInstance());
        } catch (Exception cause) {
            logger.error("Failed to remove endpoint: " + cause.getMessage(), cause);
            throw new LifecycleException("Exception while destroying servlet: " + cause.getMessage(), cause);
        }
    }

}
