package net.tvburger.up.clients.java.impl;

import net.tvburger.up.*;
import net.tvburger.up.applications.api.types.ApiEnvironmentInfo;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.deploy.*;

public final class ClientEnvironmentManager extends ApiRequester implements UpEnvironment.Manager {

    public ClientEnvironmentManager(ApiRequester requester) {
        super(requester, "manager");
    }

//    @Override
//    public void deploy(InputStream applicationDefinitionStream) throws DeployException {
//        try {
//            apiWrite("deploy/application", ApiApplicationDefinition.fromUp(applicationDefinition));
//        } catch (DeployException cause) {
//            throw cause;
//        } catch (UpException | ApiException cause) {
//            throw new ApiException("Failed to deploy application: " + cause.getMessage(), cause);
//        }
//    }

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

    @Override
    public boolean supportsPackageDefinitionType(Class<? extends UpPackageDefinition> packageDefinitionType) {
        return false;
    }

    @Override
    public UpPackage.Manager deployPackage(UpPackageDefinition packageDefinition) throws DeployException {
        return null;
    }

    @Override
    public UpApplication.Manager createApplication(String name, UpPackage.Info packageInfo) throws DeployException {
        return null;
    }

    @Override
    public UpApplication.Manager deployApplication(UpApplicationDefinition applicationDefinition, UpPackage.Info packageInfo) throws DeployException {
        return null;
    }

    @Override
    public UpService.Manager<?> deployService(UpServiceDefinition serviceDefinition, UpApplication.Info applicationInfo) throws DeployException {
        return null;
    }

    @Override
    public UpEndpoint.Manager<?> deployEndpoint(UpEndpointDefinition endpointDefinition, UpApplication.Info applicationInfo) throws DeployException {
        return null;
    }
}
