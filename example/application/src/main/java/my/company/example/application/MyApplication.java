package my.company.example.application;

import my.company.example.logic.*;
import net.tvburger.up.definition.EndpointDefinition;
import net.tvburger.up.definition.UpDeploymentDefinition;
import net.tvburger.up.technology.jsr340.Jsr340;

public final class MyApplication extends UpDeploymentDefinition {

    public MyApplication() {
        super(new UpDeploymentDefinition.Builder()
                .withServiceDefinition(DependencyService.class, DependencyServiceImpl.class)
                .withServiceDefinition(ExampleService.class, ExampleServiceImpl.class, "Howdy,", DependencyService.class)
                .withEndpointDefinition(
                        new EndpointDefinition.Builder()
                                .withEndpointTechnology(Jsr340.Specification.get())
                                .withEndpointDefinition(ExampleServlet.class, ExampleService.class)
                                .withSetting("mapping", "/hello/*")
                                .build())
                .build());
    }

}
