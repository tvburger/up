package net.tvburger.up.example.deploy;

import net.tvburger.up.definitions.ServiceDefinition;
import net.tvburger.up.definitions.UpDeploymentDefinition;
import net.tvburger.up.example.code.*;
import net.tvburger.up.technology.servlet.JSR340Specification;

public final class MyDeploymentDefinition extends UpDeploymentDefinition {

    public MyDeploymentDefinition(String prefix) {
        super(new UpDeploymentDefinition.Builder()
                .withEndpointTechnology("servlet", "3.1")
                .withServiceDefinition(DependencyService.class, DependencyServiceImpl.class)
                .withServiceDefinition(ExampleService.class, ExampleServiceImpl.class, prefix, DependencyService.class)
                .withEndpointDefinition(JSR340Specification.get(), ServiceDefinition.Factory.create(ExampleServlet.class, ExampleServlet.class, ExampleService.class), "/*")
                .build());
    }

}
