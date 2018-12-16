package net.tvburger.up.applications.api.impl;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.applications.api.types.ApiEndpointInfo;
import net.tvburger.up.security.AccessDeniedException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public final class ApiEndpoint extends ApiEntityManager<UpEndpoint.Manager<?>> {

    private final UpEnvironment environment;
    private final UpEndpoint.Info endpointInfo;

    ApiEndpoint(final UpEnvironment environment, final UpEndpoint.Info endpointInfo) {
        this.environment = environment;
        this.endpointInfo = endpointInfo;
    }

    protected UpEndpoint.Manager<?> getManager() throws AccessDeniedException {
        return environment.getEndpointManager(endpointInfo);
    }

    @Path("/info")
    @GET
    public ApiEndpointInfo getInfo() {
        return ApiEndpointInfo.fromUp(endpointInfo);
    }

}
