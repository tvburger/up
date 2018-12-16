package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpException;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.ApiServiceInfo;
import net.tvburger.up.applications.api.types.ApiSpecification;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.clients.java.ApiException;

public final class ClientServiceManager<T> extends ClientEntityManager implements UpService.Manager<T> {

    ClientServiceManager(ApiRequester requester, UpService.Info<T> serviceInfo) {
        super(requester, "service/" + serviceInfo.getIdentification().getUuid());
    }

    @SuppressWarnings("unchecked")
    @Override
    public UpService.Info<T> getInfo() {
        try {
            return (UpService.Info<T>) apiRead("info", ApiServiceInfo.class).toUp();
        } catch (ApiException | ClassNotFoundException | UpException cause) {
            throw new ApiException("Failed to read service info: " + cause.getMessage(), cause);
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
