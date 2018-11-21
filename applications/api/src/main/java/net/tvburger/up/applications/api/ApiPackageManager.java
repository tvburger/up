package net.tvburger.up.applications.api;

import net.tvburger.up.UpPackage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public final class ApiPackageManager {

    private final UpPackage.Manager manager;

    public ApiPackageManager(UpPackage.Manager manager) {
        this.manager = manager;
    }

    @Path("/info")
    @GET
    public UpPackage.Info getInfo() {
        return manager.getInfo();
    }

}
