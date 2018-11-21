package net.tvburger.up.applications.api;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.deploy.DeployException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.InputStream;
import java.util.UUID;

public final class ApiApplicationManager {

    private final UpApplication.Manager manager;

    public ApiApplicationManager(UpApplication.Manager manager) {
        this.manager = manager;
    }


// TODO: support application, serivce and endpoint definitions (?)
//    @Path("/deploy/service")
//    @POST
//    public void deploy(ApiServiceDefinition serviceDefinition, ApiApplicationDefinition applicationDefinition) throws DeployException {
//        try {
//            manager.deploy(serviceDefinition.toUp(), applicationDefinition);
//        } catch (IOException cause) {
//            throw new DeployException("Failed to deploy: " + cause.getMessage(), cause);
//        }
//    }
//
//    @Path("/deploy/endpoint")
//    @POST
//    public void deploy(ApiEndpointDefinition endpointDefinition, ApiApplicationDefinition applicationDefinition) throws DeployException {
//        try {
//            manager.deploy(endpointDefinition.toUp(), applicationDefinition);
//        } catch (IOException cause) {
//            throw new DeployException("Failed to deploy: " + cause.getMessage(), cause);
//        }
//    }

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
    public String getState() {
        return manager.getState().name();
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
    public UpApplication.Info getInfo() {
        return manager.getInfo();
    }

}
