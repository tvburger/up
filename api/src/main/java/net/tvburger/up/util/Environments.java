package net.tvburger.up.util;

import net.tvburger.up.Endpoint;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.Environment;
import net.tvburger.up.Service;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.security.AccessDeniedException;

import java.util.LinkedHashSet;
import java.util.Set;

public final class Environments {

    public static void printEnvironment(Environment environment) throws AccessDeniedException, DeployException {
        Set<Specification> endpointSpecifications = new LinkedHashSet<>();
        System.out.println("Runtime: " + environment.getRuntime().getInfo());
        for (UpEngine engine : environment.getRuntime().getEngines()) {
            System.out.println("- Engine: " + engine.getInfo());
            for (EndpointTechnologyInfo<?> endpointTechnologyInfo : engine.getEndpointTechnologies()) {
                System.out.println("  - EndpointTechnology: " + endpointTechnologyInfo);
                endpointSpecifications.add(endpointTechnologyInfo);
            }
        }
        System.out.println("Environment: " + environment.getInfo());
        for (Service<?> service : environment.getServices()) {
            System.out.println(" - Service: " + service.getInfo());
        }
        for (Specification endpointSpecification : endpointSpecifications) {
            System.out.println(" - EndpointTechnology: " + endpointSpecification);
            for (Endpoint<?, ?> endpoint : environment.getEndpoints(endpointSpecification)) {
                System.out.println("   - Endpoint: " + endpoint.getInfo());
            }
        }
    }

    private Environments() {
    }

}
