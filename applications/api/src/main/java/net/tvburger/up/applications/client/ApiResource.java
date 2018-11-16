package net.tvburger.up.applications.client;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.UpService;
import net.tvburger.up.runtime.util.UpEnvironments;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;
import java.util.Set;

@Path("/")
public final class ApiResource {

    @GET
    public Response get() {
        return Response.seeOther(URI.create("client/index.html")).build();
    }

    @Path("/api")
    @GET
    public String api() {
        return "hello world!";
    }

    @Path("/api/environmentInfo")
    @GET
    public UpEnvironment.Info getEnvironmentInfo(@QueryParam("env") String environmentName) throws UpException {
        return UpEnvironments.get(environmentName).getInfo();
    }

    @Path("/api/services")
    @GET
    public Set<UpService.Info<?>> getServices(@QueryParam("env") String environmentName) throws UpException {
        return UpEnvironments.get(environmentName).listServices();
    }

    @Path("/client/{file:.+}")
    @GET
    public InputStream getResource(@PathParam("file") String file) {
        return getClass().getClassLoader().getResourceAsStream("clients/javascript/src/main/resources/assets/" + file);
    }

}
