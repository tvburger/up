package my.company.example.logic;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class HelloResource {

    @GET
    public String sayHello() {
        return "Hello from JAX-RS!";
    }

}
