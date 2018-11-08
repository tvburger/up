package net.tvburger.up.example.deploy;

import net.tvburger.up.Environment;
import net.tvburger.up.EnvironmentManager;
import net.tvburger.up.definitions.EndpointDefinition;
import net.tvburger.up.definitions.ServiceDefinition;
import net.tvburger.up.example.code.*;
import net.tvburger.up.local.LocalUpRuntimeFactory;
import net.tvburger.up.technology.jetty9.Jetty9Implementation;
import net.tvburger.up.technology.jsr340.Jsr340;

public final class ShortExample {

    public static void main(String[] args) throws Exception {
        Environment environment = LocalUpRuntimeFactory.createEnvironment(Jetty9Implementation.get());
        try {
            EnvironmentManager manager = environment.getManager();
            manager.deploy(ServiceDefinition.Factory.create(DependencyService.class, DependencyServiceImpl.class));
            manager.deploy(ServiceDefinition.Factory.create(ExampleService.class, ExampleServiceImpl.class, "Howdy,", DependencyService.class));
            manager.deploy(new EndpointDefinition.Builder()
                    .withEndpointTechnology(Jsr340.Specification.get())
                    .withEndpointDefinition(ExampleServlet.class, ExampleService.class)
                    .withSetting("mapping", "/abc/*").build());
            Example.printEnvironment(environment);
        } finally {
            LocalUpRuntimeFactory.destroyEnvironment(environment);
        }
    }

}
