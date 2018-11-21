package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpException;
import net.tvburger.up.UpPackage;
import net.tvburger.up.applications.api.types.ApiPackageInfo;
import net.tvburger.up.clients.java.ApiException;

public final class ClientPackageManager extends ApiRequester implements UpPackage.Manager {

    public ClientPackageManager(ApiRequester requester) {
        super(requester, "manager");
    }

    @Override
    public UpPackage.Info getInfo() {
        try {
            return apiRead("info", ApiPackageInfo.class);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to read application info: " + cause.getMessage(), cause);
        }
    }

}
