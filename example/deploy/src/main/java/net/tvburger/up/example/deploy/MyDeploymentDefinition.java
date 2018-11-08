package net.tvburger.up.example.deploy;

import net.tvburger.up.definitions.EndpointDefinition;
import net.tvburger.up.definitions.UpDeploymentDefinition;
import net.tvburger.up.example.code.*;
import net.tvburger.up.technology.jsr340.Jsr340;

public final class MyDeploymentDefinition extends UpDeploymentDefinition {

    public MyDeploymentDefinition(String prefix) {
        super(new UpDeploymentDefinition.Builder()
                .withServiceDefinition(DependencyService.class, DependencyServiceImpl.class)
                .withServiceDefinition(ExampleService.class, ExampleServiceImpl.class, prefix, DependencyService.class)
                .withEndpointDefinition(
                        new EndpointDefinition.Builder()
                                .withEndpointTechnology(Jsr340.Specification.get())
                                .withEndpointDefinition(ExampleServlet.class, ExampleService.class)
                                .withSetting("mapping", "/*")
                                .build())
                .build());
    }

}
