package net.tvburger.up.applications.admin;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.UpService;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public final class EnvironmentResource {

    private final UpEnvironment environment;

    public EnvironmentResource(UpEnvironment environment) {
        this.environment = environment;
    }

    @Path("/services")
    public List<String> listServices() throws UpException {
        List<String> services = new ArrayList<>();
        for (UpService.Info<?> serviceInfo : environment.listServices()) {
            services.add(serviceInfo.toString());
        }
        return services;
    }

}
