package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpException;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.ApiApplicationInfo;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEndpointDefinition;
import net.tvburger.up.deploy.UpServiceDefinition;

public final class ClientApplicationManager extends ApiRequester implements UpApplication.Manager {

    public ClientApplicationManager(ApiRequester requester) {
        super(requester, "manager");
    }

    @Override
    public void init() throws LifecycleException {
        try {
            apiWrite("init");
        } catch (LifecycleException cause) {
            throw cause;
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to write init: " + cause.getMessage(), cause);
        }
    }

    @Override
    public void start() throws LifecycleException {
        try {
            apiWrite("start");
        } catch (LifecycleException cause) {
            throw cause;
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to write start: " + cause.getMessage(), cause);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        try {
            apiWrite("stop");
        } catch (LifecycleException cause) {
            throw cause;
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to write stop: " + cause.getMessage(), cause);
        }
    }

    @Override
    public void destroy() throws LifecycleException {
        try {
            apiWrite("destroy");
        } catch (LifecycleException cause) {
            throw cause;
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to write destroy: " + cause.getMessage(), cause);
        }
    }

    @Override
    public State getState() {
        try {
            return apiRead("state", State.class);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to read state: " + cause.getMessage(), cause);
        }
    }

    @Override
    public boolean isLogged() {
        try {
            return apiRead("logged", Boolean.class);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to read logged: " + cause.getMessage(), cause);
        }
    }

    @Override
    public void setLogged(boolean logged) {
        try {
            apiWrite("logged", logged);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to write logged: " + cause.getMessage(), cause);
        }
    }

    @Override
    public UpApplication.Info getInfo() {
        try {
            return apiRead("info", ApiApplicationInfo.class);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to read application info: " + cause.getMessage(), cause);
        }
    }

    @Override
    public UpService.Manager<?> deployService(UpServiceDefinition serviceDefinition) throws DeployException {
        return null;
    }

    @Override
    public UpEndpoint.Manager<?> deployEndpoint(UpEndpointDefinition endpointDefinition) throws DeployException {
        return null;
    }

}
