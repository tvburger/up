package net.tvburger.up.applications.admin;

import net.tvburger.up.Environment;
import net.tvburger.up.Service;
import net.tvburger.up.UpException;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public final class EnvironmentResource {

    private final Environment environment;

    public EnvironmentResource(Environment environment) {
        this.environment = environment;
    }

    @Path("/services")
    public List<String> listServices() throws UpException {
        List<String> services = new ArrayList<>();
        for (Service<?> service : environment.getServices()) {
            services.add(service.getInfo().toString());
        }
        return services;
    }

}
