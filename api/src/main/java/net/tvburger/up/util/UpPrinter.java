package net.tvburger.up.util;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Map;
import java.util.Set;

public final class UpPrinter {

    public static void printEnvironment(UpEnvironment environment) throws AccessDeniedException {
        System.out.println(String.format("[%-7s] UpEnvironment: %s", environment.getManager().getState(), environment.getInfo()));
        for (UpPackage.Info packageInfo : environment.listPackages()) {
            System.out.println(String.format("[%-7s]   UpPackage: %s", "-", packageInfo));
        }
        for (UpApplication.Info applicationInfo : environment.listApplications()) {
            UpApplication application = environment.getApplication(applicationInfo);
            System.out.println(String.format("[%-7s]   UpApplication: %s", application.getManager().getState(), applicationInfo.getName()));
            System.out.println(String.format("[%-7s]     UpPackage: %s", "-", application.getInfo().getPackageInfo().getPackageId()));
            System.out.println(String.format("[%-7s]     UpServices:", "-"));
            for (UpService.Info<?> serviceInfo : application.listServices()) {
                UpService.Manager<?> serviceManager = environment.getServiceManager(serviceInfo);
                System.out.println(String.format("[%-7s]       UpService: %s", serviceManager.getState(), serviceInfo.getSpecificationName()));
            }
            for (Map.Entry<Specification, Set<? extends UpEndpoint.Info>> entry : application.listEndpoints().entrySet()) {
                System.out.println(String.format("[%-7s]     Endpoints: %s", "-", entry.getKey()));
                for (UpEndpoint.Info endpointInfo : entry.getValue()) {
                    UpEndpoint.Manager<?> endpointManager = environment.getEndpointManager(endpointInfo);
                    if (endpointManager != null) { // TODO: fix this - it shouldn't be listed in the application.listEndpoints...(?)
                        System.out.println(String.format("[%-7s]       UpEndpoint: %s", endpointManager.getState(), endpointInfo.getEndpointUri()));
                    }
                }
            }
        }
    }

    private UpPrinter() {
    }

}
