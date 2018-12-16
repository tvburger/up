package net.tvburger.up.clients.java;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.util.Identities;

import java.util.Map;
import java.util.Set;

public final class ApiClientDemo {

    public static void main(String[] args) throws UpException {
        UpClient client = UpClient.newBuilder(new ApiClientTarget("http://Thomass-MacBook-Air.local:61454"))
                .withEnvironment("dev")
                .withIdentity(Identities.createAnonymous())
                .build();
        UpEnvironment environment = client.getEnvironment();
        for (UpPackage.Info info : environment.listPackages()) {
            UpPackage.Manager manager = environment.getPackageManager(info);
            System.out.println(manager.getInfo());
        }
        for (UpService.Info<?> serviceInfo : environment.listServices()) {
            UpService.Manager<?> manager = environment.getServiceManager(serviceInfo);
            System.out.println(manager.getInfo());
            System.out.println(manager.isLogged() + " " + manager.getState());
            if (!manager.isLogged()) {
                manager.setLogged(true);
            }
            System.out.println("We enable logging: " + manager.isLogged());
        }
        for (Map.Entry<Specification, Set<? extends UpEndpoint.Info>> entry : environment.listEndpoints().entrySet()) {
            System.out.println(entry.getValue());
            for (UpEndpoint.Info endpointInfo : entry.getValue()) {
                UpEndpoint.Manager<?> manager = environment.getEndpointManager(endpointInfo);
                System.out.println(manager.getInfo());
                System.out.println(manager.isLogged() + " " + manager.getState());
                if (!manager.isLogged()) {
                    manager.setLogged(true);
                }
                System.out.println("We enable logging: " + manager.isLogged());
            }
        }
        for (UpApplication.Info info : environment.listApplications()) {
            UpApplication application = environment.getApplication(info);
            System.out.println(application.getInfo());
            for (UpService.Info<?> serviceInfo : application.listServices()) {
                UpService.Manager<?> manager = application.getServiceManager(serviceInfo);
                System.out.println(manager.getInfo());
                System.out.println(manager.isLogged() + " " + manager.getState());
            }
            for (Map.Entry<Specification, Set<? extends UpEndpoint.Info>> entry : application.listEndpoints().entrySet()) {
                System.out.println(entry.getValue());
                for (UpEndpoint.Info endpointInfo : entry.getValue()) {
                    UpEndpoint.Manager<?> manager = application.getEndpointManager(endpointInfo);
                    System.out.println(manager.getInfo());
                    System.out.println(manager.isLogged() + " " + manager.getState());
                }
            }
        }
    }

}
