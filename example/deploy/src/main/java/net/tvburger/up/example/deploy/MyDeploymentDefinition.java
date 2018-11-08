package net.tvburger.up.example.deploy;

import net.tvburger.up.definitions.ServiceDefinition;
import net.tvburger.up.definitions.UpDeploymentDefinition;
import net.tvburger.up.example.code.*;

public final class MyDeploymentDefinition extends UpDeploymentDefinition {

    public MyDeploymentDefinition(String prefix) {
        super(new UpDeploymentDefinition.Builder()
                .withServiceType(DependencyService.class, DependencyServiceImpl.class)
                .withServiceType(ExampleService.class, ExampleServiceImpl.class)
                .withServiceType(ExampleServlet.class, ExampleServlet.class)
                .withServiceDefinition(DependencyService.class)
                .withServiceDefinition(ExampleService.class, prefix, DependencyService.class)
                .withEndpointDefinition("servlet", "3.1", ServiceDefinition.Factory.create(ExampleServlet.class, ExampleService.class), "/*")
                .build());
    }

}
