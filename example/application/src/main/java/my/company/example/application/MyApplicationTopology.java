package my.company.example.application;

import my.company.example.logic.*;
import net.tvburger.up.topology.EndpointDefinition;
import net.tvburger.up.topology.UpApplicationTopology;
import net.tvburger.up.technology.jsr340.Jsr340;

public final class MyApplicationTopology extends UpApplicationTopology {

    public MyApplicationTopology() {
        super(new UpApplicationTopology.Builder()
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
