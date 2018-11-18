package net.tvburger.up.applications.api;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.runtime.util.UpEnvironments;
import net.tvburger.up.security.AccessDeniedException;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/")
public final class ApiRoot {

    @Path("/{environmentName}")
    public Object getEnvironment(@PathParam("environmentName") String environmentName) throws AccessDeniedException {
        UpEnvironment environment = UpEnvironments.get(environmentName);
        if (environment == null) {
            return Response.status(Response.Status.NOT_FOUND);
        } else {
            return new ApiEnvironment(environment);
        }
    }

}
