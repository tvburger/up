package net.tvburger.up.runtime.util;

import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.security.AccessDeniedException;

public final class UpRuntimes {

    public static void printRuntime(UpRuntime runtime) throws AccessDeniedException {
        System.out.println(String.format("[%-7s] Runtime: %s", runtime.getManager().getState(), runtime.getInfo()));
        for (UpEngine.Info engineInfo : runtime.listEngines()) {
            System.out.println(String.format("[%-7s]   Engine: %s", runtime.getManager().getState(), engineInfo));
            for (UpEndpointTechnologyInfo technologyInfo : runtime.listEndpointTechnologies()) {
                UpEndpointTechnology.Manager<?> technologyManager = runtime.getEndpointTechnologyManager(technologyInfo);
                System.out.println(String.format("[%-7s]     UpEndpointTechnology: %s", technologyManager.getState(), technologyInfo));
            }
        }
    }

    private UpRuntimes() {
    }

}
