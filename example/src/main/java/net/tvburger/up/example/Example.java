package net.tvburger.up.example;

import net.tvburger.up.Up;
import net.tvburger.up.UpClient;

public class Example {

    public static void main(String[] args) {
        UpClient client = Up.createClientBuilder().withEnvironment("dev-env").build();
        client.addService(ExampleServiceImpl.class, "Hello %s!");
        ExampleService service = client.getService(ExampleService.class);
        client.getServiceManager(service).setLogged(true);
        service.sayHelloTo("foo bar");

        UpClient client2 = Up.createClientBuilder().withEnvironment("test-env").build();
        client2.addService(ExampleServiceImpl.class, "Hi %s!");
        ExampleService service2 = client2.getService(ExampleService.class);
        client.getServiceManager(service2).setLogged(true);
        service2.sayHelloTo("foo bar");
    }

}
