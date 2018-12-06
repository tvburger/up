package net.tvburger.up.example.deploy;

import my.company.example.application.MyApplicationDefinition;
import my.company.example.logic.ExampleService;
import my.company.example.runtime.MyDevRuntimeTopology;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.UpPackage;
import net.tvburger.up.applications.admin.AdminApplicationDefinition;
import net.tvburger.up.applications.api.ApiDefinition;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientException;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.infra.UpRuntimeTopology;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.runtime.util.UpEnvironments;
import net.tvburger.up.runtimes.local.LocalPackageDefinition;
import net.tvburger.up.runtimes.local.infra.LocalProvisioner;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jersey2.Jersey2Implementation;
import net.tvburger.up.technology.jetty9.Jetty9Implementation;
import net.tvburger.up.util.Identities;

public final class Example {

    private final UpApplicationDefinition applicationDefinition;
    private UpRuntimeTopology runtimeTopology;
    private UpClientTarget target;
    private UpEnvironment environment;

    public Example(UpApplicationDefinition applicationDefinition) {
        this.applicationDefinition = applicationDefinition;
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

    public void exampleApplication() throws UpException {
        UpPackage.Info packageInfo = environment.getManager().deployPackage(LocalPackageDefinition.Factory.create("test")).getInfo();
        environment.getManager().deployApplication(applicationDefinition, packageInfo);
    }

    private void adminApplication() throws UpException {
        UpEnvironment.Manager manager = environment.getManager();
        UpPackage.Info packageInfo = manager.deployPackage(LocalPackageDefinition.Factory.create("admin")).getInfo();
        manager.deployApplication(new AdminApplicationDefinition(), packageInfo);
    }

    private void apiApplication() throws UpException {
        UpEnvironment.Manager manager = environment.getManager();
        UpPackage.Info packageInfo = manager.deployPackage(LocalPackageDefinition.Factory.create("api")).getInfo();
        manager.deployApplication(new ApiDefinition(), packageInfo);
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
        System.out.println("_________ UpEnvironment: " + environment.getInfo().getName() + " ___________________");
        UpEnvironments.printEnvironment(environment);
        System.out.println("---------------------------------------------");
    }

    public static void main(String[] args) throws UpException {
        Example example = new Example(new MyApplicationDefinition());
        example.setRuntimeTopology(new MyDevRuntimeTopology());
        example.init();


        example.exampleApplication();
        example.adminApplication();
        example.apiApplication();
        example.start();


        example.sayHi();
        example.printEnvironment();

        allowWebAccessFor10min();

        example.stop();
        example.printEnvironment();
        example.destroy();
        example.printEnvironment();
    }

    private static void allowWebAccessFor10min() {
        try {
            Thread.sleep(600_000);
        } catch (InterruptedException cause) {
        }
    }

}
