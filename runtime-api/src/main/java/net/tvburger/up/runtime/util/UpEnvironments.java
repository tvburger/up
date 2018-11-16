package net.tvburger.up.runtime.util;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Map;
import java.util.Set;

public final class UpEnvironments {

    public static void printEnvironment(UpEnvironment environment) throws AccessDeniedException {
        System.out.println(String.format("[%-7s] UpEnvironment: %s", environment.getManager().getState(), environment.getInfo()));
        System.out.println(String.format("[%-7s]   UpServices:", "-"));
        for (UpService.Info<?> serviceInfo : environment.listServices()) {
            UpService.Manager<?> serviceManager = environment.getServiceManager(serviceInfo);
            System.out.println(String.format("[%-7s]     UpService: %s", serviceManager.getState(), serviceInfo));
        }
        for (Map.Entry<Specification, Set<? extends UpEndpoint.Info>> entry : environment.listEndpoints().entrySet()) {
            System.out.println(String.format("[%-7s]   Endpoints: %s", "-", entry.getKey()));
            for (UpEndpoint.Info endpointInfo : entry.getValue()) {
                UpEndpoint.Manager<?> endpointManager = environment.getEndpointManager(endpointInfo);
                System.out.println(String.format("[%-7s]     UpEndpoint: %s", endpointManager.getState(), endpointInfo));
            }
        }
    }

    public static UpEnvironment get(String environmentName) throws AccessDeniedException {
        return UpContext.getContext().getRuntime().getEnvironment(environmentName);
    }

    private UpEnvironments() {
    }

}
