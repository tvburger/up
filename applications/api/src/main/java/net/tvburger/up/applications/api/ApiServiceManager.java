package net.tvburger.up.applications.api;

import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.ApiSpecification;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.LifecycleManager;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

public final class ApiServiceManager {

    private final UpService.Manager<?> manager;

    public ApiServiceManager(UpService.Manager<?> manager) {
        this.manager = manager;
    }

    @Path("/implementation/name")
    @GET
    public String getImplementationName() {
        return manager.getImplementationName();
    }

    @Path("/implementation/version")
    @GET
    public String getImplementationVersion() {
        return manager.getImplementationVersion();
    }

    @Path("/specification")
    @GET
    public ApiSpecification getSpecification() {
        return ApiSpecification.fromUp(manager.getSpecification());
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
    public UpService.Info<?> getInfo() {
        return manager.getInfo();
    }

}
