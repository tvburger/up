package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpException;
import net.tvburger.up.UpPackage;
import net.tvburger.up.applications.api.types.ApiPackageInfo;
import net.tvburger.up.clients.java.ApiException;

public final class ClientPackageManager extends ApiRequester implements UpPackage.Manager {

    ClientPackageManager(ApiRequester requester, UpPackage.Info packageInfo) {
        super(requester, "package/" + packageInfo.getPackageId());
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
