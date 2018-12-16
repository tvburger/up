package net.tvburger.up.technology.jersey2;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpPackage;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEndpointDefinition;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.technology.jsr370.Jsr370;
import net.tvburger.up.util.Identities;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public final class Jersey2TechnologyManager extends LifecycleManagerImpl implements Jsr370.Manager {

    private static final Logger logger = LoggerFactory.getLogger(Jersey2TechnologyManager.class);

    private final Map<UpEnvironment.Info, Map<UpApplication.Info, Set<Jsr370.Endpoint.Info>>> environments = new HashMap<>();
    private final Map<Jsr370.Endpoint.Info, HttpServer> httpServers = new HashMap<>();
    private final Map<Jsr370.Endpoint.Info, Jersey2Endpoint> endpoints = new HashMap<>();
    private final Map<Jsr370.Endpoint.Info, UpEndpointDefinition> definitions = new HashMap<>();

    private final UpEngine engine;
    private final Identity identity;

    private String hostname;
    private boolean logged;

    Jersey2TechnologyManager(UpEngine engine, Identity identity) {
        this.engine = engine;
        this.identity = identity;
    }

    public UpEngine getEngine() {
        return engine;
    }

    @Override
    public synchronized void init() throws LifecycleException {
        super.init();
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public synchronized void start() throws LifecycleException {
        super.start();
        for (Jersey2Endpoint endpoint : endpoints.values()) {
            try {
                Jersey2Endpoint.Manager manager = endpoint.getManager();
                if (manager.getState() == State.READY) {
                    manager.doStart();
                }
            } catch (AccessDeniedException cause) {
            }
        }
    }

    @Override
    public synchronized void stop() throws LifecycleException {
        super.stop();
        for (Jersey2Endpoint endpoint : endpoints.values()) {
            try {
                Jersey2Endpoint.Manager manager = endpoint.getManager();
                if (manager.getState() == State.ACTIVE) {
                    manager.doStop();
                }
            } catch (AccessDeniedException cause) {
            }
        }
    }

    @Override
    public synchronized void destroy() throws LifecycleException {
        super.destroy();
        for (Jersey2Endpoint endpoint : endpoints.values()) {
            try {
                Jersey2Endpoint.Manager manager = endpoint.getManager();
                if (manager.getState() == State.READY) {
                    manager.doDestroy();
                }
            } catch (AccessDeniedException cause) {
            }
        }
    }

    @Override
    public Jsr370.Info getInfo() {
        return Jsr370.Info.get();
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
    public synchronized Jsr370.Endpoint.Manager deployEndpoint(UpEndpointDefinition endpointDefinition, UpApplication application, UpPackage upPackage) throws DeployException {
        try {
            if (endpointDefinition == null || application == null) {
                throw new IllegalArgumentException();
            }
            logger.info("Deploying endpoint for: " + application.getInfo());
            Jsr370.Endpoint.Definition definition = Jsr370.Endpoint.Definition.parse(endpointDefinition);
            String mapping = definition.getMapping();
            String mappingWithoutSlash = mapping.startsWith("/") ? mapping.substring(1) : mapping;
            URI uri = UriBuilder.fromPath("/" + application.getInfo().getEnvironmentInfo().getName() + "/" + mappingWithoutSlash).scheme("http").host(hostname).port(findFreePort()).build();
            Class<? extends Application> applicationClass = upPackage.getClassLoader().loadClass(definition.getApplicationSpecification(), Application.class);
            Jsr370.Endpoint.Info info = new Jsr370.Endpoint.Info(uri,
                    Identities.createAnonymous(),
                    applicationClass,
                    uri.getPort(),
                    uri.getHost(),
                    application.getInfo().getEnvironmentInfo().getName(),
                    mapping,
                    applicationClass.getCanonicalName() + "@" + uri.getPort(),
                    application.getInfo());
            Jersey2Endpoint endpoint = Jersey2Endpoint.Factory.create(info, this);
            Application instance = new Jersey2ContextApplication(applicationClass.newInstance(), application, upPackage, endpoint, identity);
            ResourceConfig resourceConfig = ResourceConfig.forApplication(instance);
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, resourceConfig);
            environments.computeIfAbsent(info.getApplicationInfo().getEnvironmentInfo(), k -> new ConcurrentHashMap<>())
                    .computeIfAbsent(application.getInfo(), k -> new CopyOnWriteArraySet<>())
                    .add(endpoint.getInfo());
            httpServers.put(info, server);
            endpoints.put(info, endpoint);
            definitions.put(info, definition);
            endpoint.getManager().doInit();
            logger.info("UpEndpoint deployed: " + info);
            return endpoint.getManager();
        } catch (LifecycleException | IllegalArgumentException | ClassNotFoundException | AccessDeniedException | InstantiationException | IllegalAccessException cause) {
            String message = "Failed to deploy endpoint: " + cause.getMessage();
            logger.error(message, cause);
            throw new DeployException(message, cause);
        }
    }

    private int findFreePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            int port = socket.getLocalPort();
            try {
                socket.close();
            } catch (IOException e) {
            }
            return port;
        } catch (IOException e) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        throw new IllegalStateException("Could not find a free TCP/IP port to start embedded JAX-RS Server on");
    }

    synchronized void start(Jsr370.Endpoint.Info info) throws LifecycleException {
        try {
            logger.info("Starting endpoint: " + info);
            endpoints.get(info).getManager().doStart();
            httpServers.get(info).start();
        } catch (IOException | AccessDeniedException cause) {
            String message = "Failed to start endpoint: " + cause.getMessage();
            logger.error(message, cause);
            throw new LifecycleException(message, cause);
        }
    }

    synchronized void stop(Jsr370.Endpoint.Info info) throws LifecycleException {
        try {
            logger.info("Stopping endpoint: " + info);
            endpoints.get(info).getManager().doStop();
            httpServers.get(info).shutdownNow();
        } catch (AccessDeniedException cause) {
            String message = "Failed to stop endpoint: " + cause.getMessage();
            logger.error(message, cause);
            throw new LifecycleException(message, cause);
        }
    }

    synchronized void destroy(Jsr370.Endpoint.Info info) throws LifecycleException {
        try {
            logger.info("Removing endpoint: " + info);
            Jersey2Endpoint endpoint = endpoints.remove(info);
            environments.get(info.getApplicationInfo().getEnvironmentInfo())
                    .get(info.getApplicationInfo())
                    .remove(endpoint.getInfo());
            definitions.remove(info);
            httpServers.remove(info);
            endpoint.getManager().doDestroy();
        } catch (Exception cause) {
            logger.error("Failed to remove endpoint: " + cause.getMessage(), cause);
            throw new LifecycleException("Exception while destroying application: " + cause.getMessage(), cause);
        }
    }

    Map<UpApplication.Info, Set<Jsr370.Endpoint.Info>> listServices(UpEnvironment.Info environmentInfo) {
        Map<UpApplication.Info, Set<Jsr370.Endpoint.Info>> endpoints = new HashMap<>();
        for (Map.Entry<UpApplication.Info, Set<Jsr370.Endpoint.Info>> entry : this.environments.get(environmentInfo).entrySet()) {
            endpoints.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
        }
        return endpoints;
    }

    UpEndpoint.Manager<Jsr370.Endpoint.Info> getEndpointManager(Jsr370.Endpoint.Info endpointInfo) throws AccessDeniedException {
        Jsr370.Endpoint endpoint = endpoints.get(endpointInfo);
        return endpoint == null ? null : endpoint.getManager();
    }

}
