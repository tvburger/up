package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpException;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.ApiApplicationInfo;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEndpointDefinition;
import net.tvburger.up.deploy.UpServiceDefinition;

public final class ClientApplicationManager extends ClientEntityManager implements UpApplication.Manager {

    ClientApplicationManager(ApiRequester requester) {
        super(requester, "manager");
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
