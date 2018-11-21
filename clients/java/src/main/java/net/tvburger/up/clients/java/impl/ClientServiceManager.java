package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpException;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.ApiServiceInfo;
import net.tvburger.up.applications.api.types.ApiSpecification;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.clients.java.ApiException;

public final class ClientServiceManager<T> extends ApiRequester implements UpService.Manager<T> {

    public ClientServiceManager(String path, ApiRequester requester) {
        super(requester, path);
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

    @SuppressWarnings("unchecked")
    @Override
    public UpService.Info<T> getInfo() {
        try {
            return (UpService.Info<T>) apiRead("info", ApiServiceInfo.class).toUp();
        } catch (ApiException | ClassNotFoundException | UpException cause) {
            throw new ApiException("Failed to read application info: " + cause.getMessage(), cause);
        }
    }

    @Override
    public String getImplementationName() {
        try {
            return apiRead("implementation/name", String.class);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to read implementation name: " + cause.getMessage(), cause);
        }
    }

    @Override
    public String getImplementationVersion() {
        try {
            return apiRead("implementation/version", String.class);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to read implementation version: " + cause.getMessage(), cause);
        }
    }

    @Override
    public Specification getSpecification() {
        try {
            return apiRead("specification", ApiSpecification.class);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to read specification: " + cause.getMessage(), cause);
        }
    }
}
