package net.tvburger.up.util;

import net.tvburger.up.Endpoint;
import net.tvburger.up.Environment;
import net.tvburger.up.Service;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.security.AccessDeniedException;

import java.util.LinkedHashSet;
import java.util.Set;

public final class Environments {

    public static Set<Endpoint<?, ?>> getEndpoints(Environment environment) throws AccessDeniedException, DeployException {
        Set<Endpoint<?, ?>> endpoints = new LinkedHashSet<>();
        for (Specification endpointSpecification : UpRuntimes.getEndpointSpecifications(environment.getRuntime())) {
            for (Endpoint<?, ?> endpoint : environment.getEndpoints(endpointSpecification)) {
                endpoints.add(endpoint);
            }
        }
        return endpoints;
    }

    public static void printEnvironment(Environment environment) throws AccessDeniedException, DeployException {
        System.out.println(String.format("[%-7s] Environment: %s", environment.getManager().getState(), environment.getInfo()));
        System.out.println(String.format("[%-7s]   Services:", "-"));
        for (Service<?> service : environment.getServices()) {
            System.out.println(String.format("[%-7s]     Service: %s", service.getManager().getState(), service.getInfo()));
        }
        for (Specification endpointSpecification : UpRuntimes.getEndpointSpecifications(environment.getRuntime())) {
            System.out.println(String.format("[%-7s]   Endpoints: %s", "-", endpointSpecification));
            for (Endpoint<?, ?> endpoint : environment.getEndpoints(endpointSpecification)) {
                System.out.println(String.format("[%-7s]     Endpoint: %s", endpoint.getManager().getState(), endpoint.getInfo()));
            }
        }
    }

    private Environments() {
    }

}
