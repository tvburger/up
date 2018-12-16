package net.tvburger.up.applications.api.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpPackage;
import net.tvburger.up.applications.api.types.ApiPackageInfo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public final class ApiPackage {

    private final UpEnvironment environment;
    private final UpPackage.Info packageInfo;

    ApiPackage(final UpEnvironment environment, final UpPackage.Info packageInfo) {
        this.environment = environment;
        this.packageInfo = packageInfo;
    }

    @Path("/info")
    @GET
    public ApiPackageInfo getInfo() {
        return ApiPackageInfo.fromUp(packageInfo);
    }

}
