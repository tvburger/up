package net.tvburger.up.applications.api;

import net.tvburger.up.applications.api.types.ApiServiceInfo;
import net.tvburger.up.security.AccessDeniedException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Path("/")
public final class ApiService {

    private final ApiServiceProxy serviceProxy;

    public ApiService(ApiServiceProxy serviceProxy) {
        this.serviceProxy = serviceProxy;
    }

    @Path("/{methodName}")
    @POST
    public String invoke(@PathParam("methodName") String methodName, String body) throws Throwable {
        try {
            return Objects.toString(serviceProxy.invoke(methodName, new Object[0]));
        } catch (InvocationTargetException cause) {
            return "error";
        }
    }

    @Path("/manager")
    public ApiServiceManager getManager() throws AccessDeniedException {
        return new ApiServiceManager(serviceProxy.getService().getManager());
    }

    @Path("/info")
    @GET
    public ApiServiceInfo getInfo() {
        return ApiServiceInfo.fromUp(serviceProxy.getService().getInfo());
    }

}
