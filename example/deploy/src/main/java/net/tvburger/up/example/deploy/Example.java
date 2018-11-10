package net.tvburger.up.example.deploy;

import my.company.example.application.MyApplication;
import my.company.example.logic.ExampleService;
import my.company.example.runtime.MyApplicationDevRuntime;
import net.tvburger.up.Environment;
import net.tvburger.up.Service;
import net.tvburger.up.Up;
import net.tvburger.up.UpException;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.definition.UpDeploymentDefinition;
import net.tvburger.up.definition.UpRuntimeDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.runtimes.local.LocalUpRuntimeFactory;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.util.Environments;
import net.tvburger.up.util.Identities;

public final class Example {

    private final UpDeploymentDefinition application;
    private final UpRuntimeDefinition runtime;

    public Example(UpDeploymentDefinition application, UpRuntimeDefinition runtime) {
        this.application = application;
        this.runtime = runtime;
    }

    public void runInLocalRuntime() throws DeployException, AccessDeniedException {
        UpClientTarget clientTarget = new LocalUpRuntimeFactory().create(runtime);

        UpClient client = Up.createClientBuilder(clientTarget)
                .withEnvironment("dev")
                .withIdentity(Identities.ANONYMOUS)
                .build();

        Environment environment = client.getEnvironment();
        environment.getManager().deploy(application);
        Environments.printEnvironment(environment);

        Service<ExampleService> service = environment.getService(ExampleService.class);
        System.out.println("> " + service.getInterface().sayHelloTo("Tom"));

        // Allow access to servlet for 1 minute before terminating the runtime (and thus application deployment)
        try {
            Thread.sleep(60_000);
        } catch (InterruptedException cause) {
        }

        LocalUpRuntimeFactory.destroy(clientTarget);
    }

    public static void main(String[] args) throws UpException {
        Example example = new Example(new MyApplication(), new MyApplicationDevRuntime());
        example.runInLocalRuntime();
    }

}
