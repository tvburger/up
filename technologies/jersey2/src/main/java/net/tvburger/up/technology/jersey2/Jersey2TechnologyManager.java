package net.tvburger.up.technology.jersey2;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.technology.jsr370.Jsr370;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.topology.UpEndpointDefinition;
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
import java.util.*;

public final class Jersey2TechnologyManager extends LifecycleManagerImpl implements Jsr370.Manager {

    private static final Logger logger = LoggerFactory.getLogger(Jersey2TechnologyManager.class);

    private final Map<UpEnvironment.Info, Set<Jsr370.Endpoint.Info>> environments = new HashMap<>();
    private final Map<Jsr370.Endpoint.Info, HttpServer> httpServers = new HashMap<>();
    private final Map<Jsr370.Endpoint.Info, Jsr370.Endpoint> endpoints = new HashMap<>();
    private final Map<Jsr370.Endpoint.Info, UpEndpointDefinition> definitions = new HashMap<>();

    private final UpEngine engine;
    private final Identity identity;

    private String hostname;
    private boolean logged;

    public Jersey2TechnologyManager(UpEngine engine, Identity identity) {
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
        for (Jsr370.Endpoint endpoint : endpoints.values()) {
            try {
                Jsr370.Endpoint.Manager manager = endpoint.getManager();
                if (manager.getState() == State.READY) {
                    manager.stop();
                }
            } catch (AccessDeniedException cause) {
            }
        }
    }

    @Override
    public synchronized void stop() throws LifecycleException {
        super.stop();
        for (Jsr370.Endpoint endpoint : endpoints.values()) {
            try {
                Jsr370.Endpoint.Manager manager = endpoint.getManager();
                if (manager.getState() == State.ACTIVE) {
                    manager.stop();
                }
            } catch (AccessDeniedException cause) {
            }
        }
    }

    @Override
    public synchronized void destroy() throws LifecycleException {
        super.destroy();
        for (Jsr370.Endpoint endpoint : endpoints.values()) {
            try {
                Jsr370.Endpoint.Manager manager = endpoint.getManager();
                if (manager.getState() == State.READY) {
                    manager.destroy();
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
    public Jsr370.Endpoint deploy(UpEnvironment.Info environmentInfo, UpEndpointDefinition endpointDefinition) throws TopologyException {
        try {
            if (environmentInfo == null || endpointDefinition == null) {
                throw new IllegalArgumentException();
            }
            logger.info("Deploying endpoint in: " + environmentInfo.getName());
            Jsr370.Endpoint.Definition definition = Jsr370.Endpoint.Definition.parse(endpointDefinition);
            String mapping = definition.getMapping();
            String mappingWithoutSlash = mapping.startsWith("/") ? mapping.substring(1) : mapping;
            URI uri = UriBuilder.fromPath("/" + environmentInfo.getName() + "/" + mappingWithoutSlash).scheme("http").host(hostname).port(findFreePort()).build();
            Jsr370.Endpoint.Info info = new Jsr370.Endpoint.Info(uri,
                    Identities.ANONYMOUS,
                    definition.getApplicationClass(),
                    uri.getPort(),
                    uri.getHost(),
                    environmentInfo.getName(),
                    mapping,
                    definition.getApplicationClass().getName() + "@" + uri.getPort(),
                    environmentInfo);
            Jersey2Endpoint endpoint = Jersey2Endpoint.Factory.create(info, this);
            Application application = new Jersey2ContextApplication(definition.getApplicationClass().newInstance(), endpoint, identity);
            ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, resourceConfig);
            environments.computeIfAbsent(info.getEnvironmentInfo(), k -> new HashSet<>()).add(endpoint.getInfo());
            httpServers.put(info, server);
            endpoints.put(info, endpoint);
            definitions.put(info, definition);
            endpoint.getManager().init();
            logger.info("UpEndpoint deployed: " + info);
            return endpoint;
        } catch (LifecycleException | AccessDeniedException | InstantiationException | IllegalAccessException cause) {
            String message = "Failed to deploy endpoint: " + cause.getMessage();
            logger.error(message, cause);
            throw new TopologyException(message, cause);
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

    void start(Jsr370.Endpoint.Info info) throws LifecycleException {
        try {
            logger.info("Starting endpoint: " + info);
            httpServers.get(info).start();
        } catch (IOException cause) {
            String message = "Failed to start endpoint: " + cause.getMessage();
            logger.error(message, cause);
            throw new LifecycleException(message, cause);
        }
    }

    void stop(Jsr370.Endpoint.Info info) {
        logger.info("Stopping endpoint: " + info);
        httpServers.get(info).shutdownNow();
    }

    void destroy(Jsr370.Endpoint.Info info) throws LifecycleException {
        try {
            logger.info("Removing endpoint: " + info);
            Jsr370.Endpoint endpoint = endpoints.remove(info);
            environments.get(info.getEnvironmentInfo()).remove(endpoint);
            definitions.remove(info);
            httpServers.remove(info);
        } catch (Exception cause) {
            logger.error("Failed to remove endpoint: " + cause.getMessage(), cause);
            throw new LifecycleException("Exception while destroying application: " + cause.getMessage(), cause);
        }
    }

    Set<Jsr370.Endpoint.Info> listServices(UpEnvironment.Info environmentInfo) {
        return environments.containsKey(environmentInfo)
                ? Collections.unmodifiableSet(environments.get(environmentInfo))
                : Collections.emptySet();
    }

    UpEndpoint.Manager<Jsr370.Endpoint.Info> getEndpointManager(Jsr370.Endpoint.Info endpointInfo) throws AccessDeniedException {
        Jsr370.Endpoint endpoint = endpoints.get(endpointInfo);
        return endpoint == null ? null : endpoint.getManager();
    }

}
