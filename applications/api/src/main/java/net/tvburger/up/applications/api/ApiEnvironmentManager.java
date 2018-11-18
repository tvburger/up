package net.tvburger.up.applications.api;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.applications.api.types.ApiApplicationTopology;
import net.tvburger.up.applications.api.types.ApiEndpointDefinition;
import net.tvburger.up.applications.api.types.ApiServiceDefinition;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.topology.TopologyException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.IOException;

public class ApiEnvironmentManager {

    private final UpEnvironment.Manager manager;

    public ApiEnvironmentManager(UpEnvironment.Manager manager) {
        this.manager = manager;
    }

    @Path("/deploy/application")
    @POST
    public void deploy(ApiApplicationTopology applicationTopology) throws TopologyException {
        try {
            manager.deploy(applicationTopology.toUp());
        } catch (ClassNotFoundException | IOException cause) {
            throw new TopologyException("Failed to deploy: " + cause.getMessage(), cause);
        }
    }

    @Path("/deploy/service")
    @POST
    public void deploy(ApiServiceDefinition serviceDefinition) throws TopologyException {
        try {
            manager.deploy(serviceDefinition.toUp());
        } catch (ClassNotFoundException | IOException cause) {
            throw new TopologyException("Failed to deploy: " + cause.getMessage(), cause);
        }
    }

    @Path("/deploy/endpoint")
    @POST
    public void deploy(ApiEndpointDefinition endpointDefinition) throws TopologyException {
        try {
            manager.deploy(endpointDefinition.toUp());
        } catch (ClassNotFoundException | IOException cause) {
            throw new TopologyException("Failed to deploy: " + cause.getMessage(), cause);
        }
    }

    @Path("/dump")
    @GET
    public ApiApplicationTopology dump() {
        return ApiApplicationTopology.fromUp(manager.dump());
    }

    @Path("/init")
    @POST
    public void init() throws LifecycleException {
        manager.init();
    }

    @Path("/start")
    @POST
    public void start() throws LifecycleException {
        manager.start();
    }

    @Path("/stop")
    @POST
    public void stop() throws LifecycleException {
        manager.stop();
    }

    @Path("/destroy")
    @POST
    public void destroy() throws LifecycleException {
        manager.destroy();
    }

    @Path("/state")
    @GET
    public LifecycleManager.State getState() {
        return manager.getState();
    }

    @Path("/logged")
    @GET
    public boolean isLogged() {
        return manager.isLogged();
    }

    @Path("/logged")
    @POST
    public void setLogged(boolean logged) {
        manager.setLogged(logged);
    }

    @Path("/info")
    @GET
    public UpEnvironment.Info getInfo() {
        return manager.getInfo();
    }

}
