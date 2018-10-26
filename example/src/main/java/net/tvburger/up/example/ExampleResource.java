package net.tvburger.up.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class ExampleResource {

    private final ExampleService service;

    public ExampleResource(ExampleService service) {
        this.service = service;
    }

    @GET
    public String sayHello(String name) {
        return service.sayHelloTo(name);
    }

}
