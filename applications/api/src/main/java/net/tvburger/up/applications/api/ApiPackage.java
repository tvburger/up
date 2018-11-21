package net.tvburger.up.applications.api;

import net.tvburger.up.UpPackage;
import net.tvburger.up.security.AccessDeniedException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public final class ApiPackage {

    private final UpPackage upPackage;

    public ApiPackage(UpPackage upPackage) {
        this.upPackage = upPackage;
    }

    @Path("/manager")
    public ApiPackageManager getManager() throws AccessDeniedException {
        return new ApiPackageManager(upPackage.getManager());
    }

    @Path("/info")
    @GET
    public UpPackage.Info getInfo() {
        return upPackage.getInfo();
    }

}
