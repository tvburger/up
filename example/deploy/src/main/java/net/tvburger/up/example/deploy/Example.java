package net.tvburger.up.example.deploy;

import my.company.example.application.MyApplicationTopology;
import my.company.example.logic.ExampleService;
import my.company.example.runtime.MyDevRuntimeTopology;
import net.tvburger.up.Environment;
import net.tvburger.up.Service;
import net.tvburger.up.Up;
import net.tvburger.up.UpException;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtimes.local.LocalUpRuntimeFactory;
import net.tvburger.up.security.AccessDeniedException;
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
            environment = LocalUpRuntimeFactory.createEnvironment(Jetty9Implementation.get());
        }
    }

    public void run() throws UpException {
        environment.getManager().deploy(applicationTopology);
        Environments.printEnvironment(environment);

        environment.getManager().start();

        Service<ExampleService> service = environment.getService(ExampleService.class);
        System.out.println(service.getInterface().sayHelloTo("Jordan"));
    }

    public void destroy() throws UpException {
        environment.getManager().stop();
        environment.getManager().destroy();
        if (target != null) {
            LocalUpRuntimeFactory.destroy(target);
        } else {
            LocalUpRuntimeFactory.destroyEnvironment(environment);
        }
    }

    public static void main(String[] args) throws UpException {
        Example example = new Example(new MyApplicationTopology());

        example.setRuntimeTopology(new MyDevRuntimeTopology()); // this is optional

        example.init();
        example.run();

        allowWebAccessFor60secs();

        example.destroy();
    }

    private static void allowWebAccessFor60secs() {
        try {
            Thread.sleep(60_000);
        } catch (InterruptedException cause) {
        }
    }

}
