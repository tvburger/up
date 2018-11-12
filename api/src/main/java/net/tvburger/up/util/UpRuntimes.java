package net.tvburger.up.util;

import net.tvburger.up.behaviors.Specification;
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

    private UpRuntimes() {
    }

}
