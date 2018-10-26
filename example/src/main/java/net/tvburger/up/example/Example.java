package net.tvburger.up.example;

import net.tvburger.up.Up;
import net.tvburger.up.UpClient;
import net.tvburger.up.proto.JaxrsProtocolManager;

import javax.ws.rs.client.ClientBuilder;

public class Example {

    public static void main(String[] args) {
        UpClient client = Up.createClient();
        client.addService(DependencyServiceImpl.class);
        client.addService(ExampleServiceImpl.class, "Hello", DependencyService.class);
        client.getServiceManager(client.getService(DependencyService.class)).setLogged(true);
        ExampleService exampleService = client.getService(ExampleService.class);
        client.getServiceManager(exampleService).setLogged(true);
        exampleService.sayHelloTo("test");

        client.getProtocol(JaxrsProtocolManager.class).registerResourceSingleton(ExampleResource.class, ExampleService.class);
        System.out.println(ClientBuilder.newClient().target("http://localhost:8080/").request().header("host", client.getEnvironment()).get());
    }

}
