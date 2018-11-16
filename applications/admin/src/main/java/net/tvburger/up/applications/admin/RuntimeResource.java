package net.tvburger.up.applications.admin;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.context.UpContext;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.*;

@Produces("application/json")
@Path("/")
public final class RuntimeResource {

    private final Map<String, EnvironmentResource> environments = new HashMap<>();
    private final Map<String, EnvironmentResource> engines = new HashMap<>();
    private final UpRuntime runtime;

    public RuntimeResource(UpRuntime runtime) {
        this.runtime = runtime;
    }

    @GET
    public Map<String, Object> get() throws UpException {
        List<Object> environments = new LinkedList<>();
        for (String environmentName : runtime.listEnvironments()) {
            environments.add(environmentName);
        }
        Map<Object, String> engines = new LinkedHashMap<>();
        for (UpEngine.Info engineInfo : runtime.listEngines()) {
            engines.put(engineInfo.getUuid(), engineInfo.getSpecificationName());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("info", runtime.getInfo());
        result.put("environments", environments);
        result.put("technologies", runtime.listEndpointTechnologies());
        result.put("engines", engines);
        return result;
    }

    @GET
    @Path("/endpoints")
    public Map<String, Object> getEndpoints() throws UpException {
        Map<String, Object> endpoints = new LinkedHashMap<>();
        for (String environmentName : runtime.listEnvironments()) {
            Map<Object, Object> managers = new LinkedHashMap<>();
//            for (UpEndpoint<?, ?> endpoint : Environments.getEndpoints(runtime.getEnvironment(environmentName))) {
//                managers.put(endpoint.getInfo().toString(), endpoint.getManager().getState());
//            }
            endpoints.put(environmentName, managers);
        }
        return endpoints;
    }

    @GET
    @Path("/environment/{name}")
    public Map<String, Object> getEnvironment(@PathParam("name") String name) throws UpException {
        UpEnvironment environment = runtime.getEnvironment(name);
        Map<Object, Object> services = new LinkedHashMap<>();
//        for (UpService<?> service : environment.getServices()) {
//            services.put(service.getInfo().toString(), service.getManager().getState());
//        }
//        Map<Object, Object> endpoints = new LinkedHashMap<>();
//        for (UpEndpoint<?, ?> endpoint : Environments.getEndpoints(environment)) {
//            endpoints.put(endpoint.getInfo().toString(), endpoint.getManager().getState());
//        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("services", services);
//        result.put("endpoints", endpoints);
        return result;
    }

    @GET
    @Path("/context")
    public Map<String, Object> getContext() {
        UpContext context = UpContext.getContext();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("transaction", context.getTransactionInfo());
        result.put("caller", context.getCallerInfo());
        return result;
    }

}
