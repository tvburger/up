package net.tvburger.up.applications.api.impl;

import net.tvburger.up.applications.api.types.ApiIdentification;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.security.AccessDeniedException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

public abstract class ApiEntityManager<T> {

    protected abstract T getManager() throws AccessDeniedException;

    protected abstract ManagedEntity.Info getInfo();

    private LifecycleManager getLifecycleManager() throws AccessDeniedException {
        return (LifecycleManager) getManager();
    }

    @Path("/identification")
    @GET
    public ApiIdentification getIdentification() {
        return ApiIdentification.fromUp(getInfo().getIdentification());
    }

    @Path("/init")
    @POST
    public void init() throws LifecycleException, AccessDeniedException {
        getLifecycleManager().init();
    }

    @Path("/start")
    @POST
    public void start() throws LifecycleException, AccessDeniedException {
        getLifecycleManager().start();
    }

    @Path("/stop")
    @POST
    public void stop() throws LifecycleException, AccessDeniedException {
        getLifecycleManager().stop();
    }

    @Path("/destroy")
    @POST
    public void destroy() throws LifecycleException, AccessDeniedException {
        getLifecycleManager().destroy();
    }

    @Path("/state")
    @GET
    public String getState() throws AccessDeniedException {
        return getLifecycleManager().getState().name();
    }

    private LogManager getLogManager() throws AccessDeniedException {
        return (LogManager) getManager();
    }

    @Path("/logged")
    @GET
    public boolean isLogged() throws AccessDeniedException {
        return getLogManager().isLogged();
    }

    @Path("/logged")
    @POST
    public void enableLogging() throws AccessDeniedException {
        getLogManager().setLogged(true);
    }

    @Path("/logged")
    @DELETE
    public void disableLogging() throws AccessDeniedException {
        getLogManager().setLogged(false);
    }

}
