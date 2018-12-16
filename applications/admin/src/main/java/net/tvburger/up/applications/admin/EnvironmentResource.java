package net.tvburger.up.applications.admin;

import net.tvburger.up.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Path("/")
@Produces("application/json")
public final class EnvironmentResource {

    private final UpEnvironment environment;

    EnvironmentResource(UpEnvironment environment) {
        this.environment = environment;
    }

    @GET
    public String getInfo() throws UpException {
        return environment.getInfo().toString();
    }

    @Path("/packages")
    @GET
    public List<String> listPackages() throws UpException {
        List<String> packages = new ArrayList<>();
        for (UpPackage.Info info : environment.listPackages()) {
            packages.add(info.toString());
        }
        return packages;
    }

    @Path("/applications")
    @GET
    public List<String> listApplications() throws UpException {
        List<String> applications = new ArrayList<>();
        for (UpApplication.Info info : environment.listApplications()) {
            applications.add(info.toString());
        }
        return applications;
    }

    @Path("/services")
    @GET
    public List<String> listServices() throws UpException {
        List<String> services = new ArrayList<>();
        for (UpService.Info<?> serviceInfo : environment.listServices()) {
            services.add(serviceInfo.toString());
        }
        return services;
    }

    @Path("/endpoints")
    @GET
    public List<String> listEndpoints() throws UpException {
        List<String> endpoints = new ArrayList<>();
        for (Set<? extends UpEndpoint.Info> entry : environment.listEndpoints().values()) {
            for (UpEndpoint.Info info : entry) {
                endpoints.add(info.toString());
            }
        }
        return endpoints;
    }

}
