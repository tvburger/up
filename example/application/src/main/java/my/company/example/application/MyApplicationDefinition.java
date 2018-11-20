package my.company.example.application;

import my.company.example.logic.*;
import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.technology.jsr340.Jsr340;
import net.tvburger.up.technology.jsr370.Jsr370;

public final class MyApplicationDefinition extends UpApplicationDefinition {

    public MyApplicationDefinition() {
        super("my_application", new UpApplicationDefinition.Builder()
                .withServiceDefinition(DependencyService.class, DependencyServiceImpl.class)
                .withServiceDefinition(ExampleService.class, ExampleServiceImpl.class, "Howdy,", DependencyService.class)
                .withEndpointDefinition(
                        new Jsr340.Endpoint.Definition.Builder()
                                .withServletInstance(ExampleServlet.class, ExampleService.class)
                                .withMapping("/example/*")
                                .build())
                .withEndpointDefinition(
                        new Jsr340.Endpoint.Definition.Builder()
                                .withServletClass(HelloServlet.class)
                                .withMapping("/hello")
                                .withInitParameter("message", "Hello World!")
                                .build())
                .withEndpointDefinition(
                        Jsr340.Endpoint.Definition.Factory.createStatic("/static/*", "/www"))
                .withEndpointDefinition(
                        Jsr370.Endpoint.Definition.Factory.create(HelloApplication.class))
                .build());
    }

}
