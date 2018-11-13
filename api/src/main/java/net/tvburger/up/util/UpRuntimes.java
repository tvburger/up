package net.tvburger.up.util;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;

import java.util.LinkedHashSet;
import java.util.Set;

public final class UpRuntimes {

    public static Set<Specification> getEndpointSpecifications(UpRuntime runtime) throws AccessDeniedException {
        Set<Specification> endpointSpecifications = new LinkedHashSet<>();
        for (UpEngine runtimeEngine : runtime.getEngines()) {
            endpointSpecifications.addAll(runtimeEngine.getEndpointTechnologies());
        }
        return endpointSpecifications;
    }

    public static void printRuntime(UpRuntime runtime) throws AccessDeniedException, DeployException {
        System.out.println(String.format("[%-7s] Runtime: %s", runtime.getManager().getState(), runtime.getInfo()));
        for (UpEngine engine : runtime.getEngines()) {
            System.out.println(String.format("[%-7s]   Engine: %s", engine.getManager().getState(), engine.getInfo()));
            for (EndpointTechnologyInfo<?> endpointTechnologyInfo : engine.getEndpointTechnologies()) {
                EndpointTechnology<?> technology = engine.getEndpointTechnology(endpointTechnologyInfo);
                System.out.println(String.format("[%-7s]     EndpointTechnology: %s", technology.getManager().getState(), endpointTechnologyInfo));
            }
        }
    }

    private UpRuntimes() {
    }

}
