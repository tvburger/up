package net.tvburger.up.example;

import net.tvburger.up.Up;
import net.tvburger.up.UpClient;

public class Example {

    public static void main(String[] args) {
        UpClient client = Up.createClientBuilder().withEnvironment("dev-env").build();
        client.addService(ExampleServiceImpl.class, "Hello %s!");
        ExampleService service = client.getService(ExampleService.class);
        System.out.println(service.sayHelloTo("dev"));

        UpClient client2 = Up.createClientBuilder().withEnvironment("test-env").build();
        client2.addService(ExampleServiceImpl.class, "Hi %s!");
        ExampleService service2 = client2.getService(ExampleService.class);
        System.out.println(service2.sayHelloTo("test"));
    }

}
