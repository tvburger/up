package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpException;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.clients.java.ApiException;

public class ClientEntityManager extends ApiRequester implements LogManager, LifecycleManager {

    protected ClientEntityManager(ApiRequester requester) {
        super(requester);
    }

    protected ClientEntityManager(ApiRequester requester, String target) {
        super(requester, target);
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
    public LifecycleManager.State getState() {
        try {
            return apiRead("state", LifecycleManager.State.class);
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

}
