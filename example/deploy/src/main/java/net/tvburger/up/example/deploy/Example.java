package net.tvburger.up.example.deploy;

import my.company.example.application.MyApplicationTopology;
import my.company.example.logic.ExampleService;
import my.company.example.runtime.MyDevRuntimeTopology;
import net.tvburger.up.*;
import net.tvburger.up.applications.admin.AdminApplicationTopology;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtimes.local.LocalUpRuntimeFactory;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jersey2.Jersey2Implementation;
import net.tvburger.up.technology.jetty9.Jetty9Implementation;
import net.tvburger.up.topology.UpApplicationTopology;
import net.tvburger.up.topology.UpRuntimeTopology;
import net.tvburger.up.util.Environments;
import net.tvburger.up.util.Identities;

public final class Example {

    private final UpApplicationTopology applicationTopology;
    private UpRuntimeTopology runtimeTopology;
    private UpClientTarget target;
    private Environment environment;

    public Example(UpApplicationTopology applicationTopology) {
        this.applicationTopology = applicationTopology;
    }

    public void setRuntimeTopology(UpRuntimeTopology runtimeTopology) {
        this.runtimeTopology = runtimeTopology;
    }

    public void init() throws DeployException, AccessDeniedException {
        if (runtimeTopology != null) {
            target = new LocalUpRuntimeFactory().create(runtimeTopology);
            UpClient client = Up.createClientBuilder(target)
                    .withEnvironment("dev")
                    .withIdentity(Identities.ANONYMOUS)
                    .build();
            environment = client.getEnvironment();
        } else {
            environment = LocalUpRuntimeFactory.createEnvironment(
                    Jetty9Implementation.get(),
                    Jersey2Implementation.get());
        }
    }

    private void adminApplication() throws UpException {
        UpClient adminClient = Up.createClientBuilder(environment.getRuntime().getClientTarget())
                .withEnvironment("admin")
                .withIdentity(Identities.ANONYMOUS)
                .build();
        EnvironmentManager manager = adminClient.getEnvironment().getManager();
        manager.deploy(new AdminApplicationTopology());
        manager.start();
    }

    public void deploy() throws UpException {
        environment.getManager().deploy(applicationTopology);
    }

    public void start() throws UpException {
        environment.getManager().start();
    }

    public void sayHi() throws UpException {
        Service<ExampleService> service = environment.getService(ExampleService.class);
        System.out.println(service.getInterface().sayHelloTo("Jordan"));
    }

    public void stop() throws UpException {
        environment.getManager().stop();
    }

    public void destroy() throws UpException {
        environment.getManager().destroy();
        if (target != null) {
            LocalUpRuntimeFactory.destroy(target);
        } else {
            LocalUpRuntimeFactory.destroyEnvironment(environment);
        }
    }

    public void printEnvironment() throws UpException {
        printEnvironment(environment);
    }

    public void printAdmin() throws UpException {
        printEnvironment(environment.getRuntime().getEnvironment("admin"));
    }

    private void printEnvironment(Environment environment) throws UpException {
        System.out.println("_________ Environment: " + environment.getInfo().getName() + " ___________________");
        Environments.printEnvironment(environment);
        System.out.println("---------------------------------------------");
    }

    public static void main(String[] args) throws UpException {
        Example example = new Example(new MyApplicationTopology());
        example.setRuntimeTopology(new MyDevRuntimeTopology()); // this is optional
        example.init();

        example.adminApplication();

        example.deploy();
        example.start();

        example.sayHi();
        example.printEnvironment();
        example.printAdmin();

        allowWebAccessFor60secs();

        example.stop();
        example.destroy();
    }

    private static void allowWebAccessFor60secs() {
        try {
            Thread.sleep(60_000);
        } catch (InterruptedException cause) {
        }
    }

}
