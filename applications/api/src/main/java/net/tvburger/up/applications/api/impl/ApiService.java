package net.tvburger.up.applications.api.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.applications.api.types.ApiServiceInfo;
import net.tvburger.up.applications.api.types.ApiSpecification;
import net.tvburger.up.security.AccessDeniedException;

import javax.ws.rs.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public final class ApiService extends ApiEntityManager<UpService.Manager<?>> {

    private final UpEnvironment environment;
    private final UpService.Info<?> serviceInfo;

    ApiService(final UpEnvironment environment, final UpService.Info<?> serviceInfo) {
        this.environment = environment;
        this.serviceInfo = serviceInfo;
    }

    protected UpService.Manager<?> getManager() throws AccessDeniedException {
        return environment.getServiceManager(serviceInfo);
    }

    protected UpService.Info<?> getInfo() {
        return serviceInfo;
    }

    @Path("/info")
    @GET
    public ApiServiceInfo getServiceInfo() {
        return ApiServiceInfo.fromUp(getInfo());
    }

    @Path("/specification")
    @GET
    public ApiSpecification getSpecification() {
        return ApiSpecification.fromUp(getInfo());
    }

    @Path("/implementation/name")
    @GET
    public String getImplementationName() throws AccessDeniedException {
        return getManager().getImplementationName();
    }

    @Path("/implementation/version")
    @GET
    public String getImplementationVersion() throws AccessDeniedException {
        return getManager().getImplementationVersion();
    }

    // TODO: implement more mature calling service method
    @Path("/method/{methodName}")
    @POST
    public String invoke(@PathParam("methodName") final String methodName, final String body) throws AccessDeniedException {
        Object service = environment.getService(getInfo());
        for (Method method : service.getClass().getMethods()) {
            if (method.getName().equals(methodName)) {
                try {
                    Object result = method.invoke(service);
                    return Objects.toString(result);
                } catch (IllegalAccessException cause) {
                    throw new AccessDeniedException(cause);
                } catch (InvocationTargetException cause) {
                    return "ERROR";
                }
            }
        }
        throw new NotFoundException();
    }

}
