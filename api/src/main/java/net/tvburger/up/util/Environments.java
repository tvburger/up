package net.tvburger.up.util;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;

import java.util.LinkedHashSet;
import java.util.Set;

public final class Environments {

    public static Set<Endpoint<?, ?>> getEndpoints(Environment environment) throws AccessDeniedException, DeployException {
        Set<Endpoint<?, ?>> endpoints = new LinkedHashSet<>();
        for (Specification endpointSpecification : getEndpointSpecifications(environment)) {
            for (Endpoint<?, ?> endpoint : environment.getEndpoints(endpointSpecification)) {
                endpoints.add(endpoint);
            }
        }
        return endpoints;
    }

    public static Set<Specification> getEndpointSpecifications(Environment environment) throws AccessDeniedException {
        Set<Specification> endpointSpecifications = new LinkedHashSet<>();
        for (UpEngine runtimeEngine : environment.getRuntime().getEngines()) {
            endpointSpecifications.addAll(runtimeEngine.getEndpointTechnologies());
        }
        return endpointSpecifications;
    }

    public static void printEnvironment(Environment environment) throws AccessDeniedException, DeployException {
        Set<Specification> endpointSpecifications = new LinkedHashSet<>();
        UpRuntime runtime = environment.getRuntime();
        System.out.println(String.format("[%-7s] Runtime: %s", runtime.getManager().getState(), runtime.getInfo()));
        for (UpEngine engine : environment.getRuntime().getEngines()) {
            System.out.println(String.format("[%-7s]   Engine: %s", engine.getManager().getState(), engine.getInfo()));
            for (EndpointTechnologyInfo<?> endpointTechnologyInfo : engine.getEndpointTechnologies()) {
                EndpointTechnology<?> technology = engine.getEndpointTechnology(endpointTechnologyInfo);
                System.out.println(String.format("[%-7s]     EndpointTechnology: %s", technology.getManager().getState(), endpointTechnologyInfo));
                endpointSpecifications.add(endpointTechnologyInfo);
            }
        }
        System.out.println(String.format("[%-7s] Environment: %s", environment.getManager().getState(), environment.getInfo()));
        System.out.println(String.format("[%-7s]   Services:", "-"));
        for (Service<?> service : environment.getServices()) {
            System.out.println(String.format("[%-7s]     Service: %s", service.getManager().getState(), service.getInfo()));
        }
        for (Specification endpointSpecification : endpointSpecifications) {
            System.out.println(String.format("[%-7s]   Endpoints: %s", "-", endpointSpecification));
            for (Endpoint<?, ?> endpoint : environment.getEndpoints(endpointSpecification)) {
                System.out.println(String.format("[%-7s]     Endpoint: %s", endpoint.getManager().getState(), endpoint.getInfo()));
            }
        }
    }

    private Environments() {
    }

}
