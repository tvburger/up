package net.tvburger.up.example.deploy;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.definitions.UpRuntimeDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.deploy.UpRuntimeFactory;
import net.tvburger.up.example.code.DependencyService;
import net.tvburger.up.example.code.ExampleService;
import net.tvburger.up.local.LocalUpRuntimeFactory;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.util.Identities;

import java.util.LinkedHashSet;
import java.util.Set;

public final class Example {

    public static void main(String[] args) throws InterruptedException, UpException {
        UpClientTarget clientTarget = prepareInfrastructure(new MyRuntimeDefinition());

        UpClient client = Up.createClientBuilder(clientTarget)
                .withEnvironment("dev")
                .withIdentity(Identities.ANONYMOUS)
                .build();

        deployApplication(client, new MyDeploymentDefinition("<DEV> Hello"));
        configureEnvironment(client);
        useApplicationThroughClient(client);

        UpClient client2 = Up.createClientBuilder(clientTarget)
                .withEnvironment("test")
                .withIdentity(Identities.ANONYMOUS)
                .build();
        deployApplication(client2, new MyDeploymentDefinition("Hello"));
        configureEnvironment(client2);
        useApplicationThroughClient(client2);
        printEnvironment(client.getEnvironment());

        Thread.sleep(60_000);

        client.getEnvironment().getRuntime().getManager().stop();
        client.getEnvironment().getRuntime().getManager().destroy();
    }

    private static UpClientTarget prepareInfrastructure(UpRuntimeDefinition runtimeDefinition) throws DeployException {
        UpRuntimeFactory runtimeFactory = new LocalUpRuntimeFactory();
        return runtimeFactory.create(runtimeDefinition);
    }

    private static void deployApplication(UpClient client, MyDeploymentDefinition deploymentDefinition) throws DeployException, AccessDeniedException {
        client.getEnvironment().getManager().deploy(deploymentDefinition);
    }

    private static void configureEnvironment(UpClient client) throws AccessDeniedException, DeployException {
        Environment environment = client.getEnvironment();
        environment.getService(ExampleService.class).getManager().setLogged(true);
        environment.getService(DependencyService.class).getManager().setLogged(true);
    }

    private static void useApplicationThroughClient(UpClient client) throws AccessDeniedException, DeployException {
        System.out.println("> " + client.getEnvironment().getService(ExampleService.class).getInterface().sayHelloTo("Tom"));
    }

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

}
