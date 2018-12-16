package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpException;
import net.tvburger.up.applications.api.types.ApiEndpointInfo;
import net.tvburger.up.clients.java.ApiException;

public final class ClientEndpointManager<I extends UpEndpoint.Info> extends ClientEntityManager implements UpEndpoint.Manager<I> {

    ClientEndpointManager(ApiRequester requester, I serviceInfo) {
        super(requester, "endpoint/" + serviceInfo.getIdentification().getUuid());
    }

    @SuppressWarnings("unchecked")
    @Override
    public I getInfo() {
        try {
            return (I) apiRead("info", ApiEndpointInfo.class).toUp();
        } catch (ApiException | ClassCastException | UpException cause) {
            throw new ApiException("Failed to read service info: " + cause.getMessage(), cause);
        }
    }

}
