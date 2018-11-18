package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.applications.api.types.ApiApplicationTopology;
import net.tvburger.up.applications.api.types.ApiEndpointDefinition;
import net.tvburger.up.applications.api.types.ApiEnvironmentInfo;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.topology.UpApplicationTopology;
import net.tvburger.up.topology.UpEndpointDefinition;
import net.tvburger.up.topology.UpServiceDefinition;

import java.io.IOException;

public final class ClientEnvironmentManager extends ApiRequester implements UpEnvironment.Manager {

    public ClientEnvironmentManager(ApiRequester requester) {
        super(requester, "manager");
    }

    @Override
    public void deploy(UpApplicationTopology applicationTopology) throws TopologyException {
        try {
            apiWrite("deploy/application", ApiApplicationTopology.fromUp(applicationTopology));
        } catch (TopologyException cause) {
            throw cause;
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to deploy application: " + cause.getMessage(), cause);
        }
    }

    @Override
    public void deploy(UpServiceDefinition serviceDefinition) throws TopologyException {
        try {
            apiWrite("deploy/service", serviceDefinition);
        } catch (TopologyException cause) {
            throw cause;
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to deploy service: " + cause.getMessage(), cause);
        }
    }

    @Override
    public void deploy(UpEndpointDefinition endpointDefinition) throws TopologyException {
        try {
            apiWrite("deploy/endpoint", ApiEndpointDefinition.fromUp(endpointDefinition));
        } catch (TopologyException cause) {
            throw cause;
        } catch (UpException | ApiException cause) {
            throw new ApiException("Failed to deploy endpoint: " + cause.getMessage(), cause);
        }
    }

    @Override
    public UpApplicationTopology dump() {
        try {
            return apiRead("dump", ApiApplicationTopology.class).toUp();
        } catch (UpException | ClassNotFoundException | IOException | ApiException cause) {
            throw new ApiException("Failed to dump: " + cause.getMessage(), cause);
        }
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
    public UpEnvironment.Info getInfo() {
        try {
            return apiRead("info", ApiEnvironmentInfo.class);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to read environment info: " + cause.getMessage(), cause);
        }
    }

}
