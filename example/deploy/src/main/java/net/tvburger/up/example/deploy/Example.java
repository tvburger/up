package net.tvburger.up.example.deploy;

import my.company.example.application.MyApplicationTopology;
import my.company.example.logic.ExampleService;
import my.company.example.runtime.MyDevRuntimeTopology;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.applications.admin.AdminApplicationTopology;
import net.tvburger.up.applications.client.ApiApplicationTopology;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientException;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.runtime.util.UpEnvironments;
import net.tvburger.up.runtimes.local.infra.LocalProvisioner;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jersey2.Jersey2Implementation;
import net.tvburger.up.technology.jetty9.Jetty9Implementation;
import net.tvburger.up.topology.UpApplicationTopology;
import net.tvburger.up.topology.UpRuntimeTopology;
import net.tvburger.up.util.Identities;

public final class Example {

    private final UpApplicationTopology applicationTopology;
    private UpRuntimeTopology runtimeTopology;
    private UpClientTarget target;
    private UpEnvironment environment;
    private UpClient adminClient;
    private UpClient apiClient;

    public Example(UpApplicationTopology applicationTopology) {
        this.applicationTopology = applicationTopology;
    }

    public void setRuntimeTopology(UpRuntimeTopology runtimeTopology) {
        this.runtimeTopology = runtimeTopology;
    }

    public void init() throws UpRuntimeException, UpClientException, AccessDeniedException {
        if (runtimeTopology != null) {
            target = new LocalProvisioner().provision(runtimeTopology);
            UpClient client = UpClient.newBuilder(target)
                    .withEnvironment("dev")
                    .withIdentity(Identities.ANONYMOUS)
                    .build();
            environment = client.getEnvironment();
        } else {
            environment = LocalProvisioner.createEnvironment(
                    Jetty9Implementation.get(),
                    Jersey2Implementation.get());
        }
    }

    private void adminApplication() throws UpException {
        adminClient = UpClient.newBuilder(target)
                .withEnvironment("admin")
                .withIdentity(Identities.ANONYMOUS)
                .build();
        UpEnvironment.Manager manager = adminClient.getEnvironment().getManager();
        manager.deploy(new AdminApplicationTopology());
        manager.start();
    }

    private void apiApplication() throws UpException {
        apiClient = UpClient.newBuilder(target)
                .withEnvironment("api")
                .withIdentity(Identities.ANONYMOUS)
                .build();
        UpEnvironment.Manager manager = apiClient.getEnvironment().getManager();
        manager.deploy(new ApiApplicationTopology());
        manager.start();
    }

    public void deploy() throws UpException {
        environment.getManager().deploy(applicationTopology);
    }

    public void start() throws UpException {
        environment.getManager().start();
    }

    public void sayHi() throws UpException {
        ExampleService exampleService = environment.lookupService(ExampleService.class);
        System.out.println(exampleService.sayHelloTo("Jordan"));
    }

    public void stop() throws UpException {
        environment.getManager().stop();
    }

    public void destroy() throws UpException {
        environment.getManager().destroy();
        if (target != null) {
            LocalProvisioner.cleanUp(target);
        } else {
            LocalProvisioner.destroyEnvironment(environment);
        }
    }

    public void printEnvironment() throws UpException {
        printEnvironment(environment);
    }

    public void printAdmin() throws UpException {
        printEnvironment(adminClient.getEnvironment());
    }

    public void printApi() throws UpException {
        printEnvironment(apiClient.getEnvironment());
    }

    private void printEnvironment(UpEnvironment environment) throws UpException {
        System.out.println("_________ UpEnvironment: " + environment.getInfo().getName() + " ___________________");
        UpEnvironments.printEnvironment(environment);
        System.out.println("---------------------------------------------");
    }

    public static void main(String[] args) throws UpException {
        Example example = new Example(new MyApplicationTopology());
        example.setRuntimeTopology(new MyDevRuntimeTopology()); // this is optional
        example.init();

        example.adminApplication();
        example.apiApplication();

        example.deploy();
        example.start();

        example.sayHi();
        example.printEnvironment();
        example.printAdmin();
        example.printApi();

        allowWebAccessFor60secs();

        example.stop();
        example.destroy();
    }

    private static void allowWebAccessFor60secs() {
        try {
            Thread.sleep(6_000_000);
        } catch (InterruptedException cause) {
        }
    }

}
