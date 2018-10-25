package net.tvburger.up.example;

import net.tvburger.up.Up;
import net.tvburger.up.UpClient;

public class Example {

    public static void main(String[] args) {
        UpClient client = Up.createClient();
        client.addService(DependencyServiceImpl.class);
        client.addService(ExampleServiceImpl.class, "Hello", DependencyService.class);
        client.getServiceManager(client.getService(DependencyService.class)).setLogged(true);
        ExampleService exampleService = client.getService(ExampleService.class);
        client.getServiceManager(exampleService).setLogged(true);
        exampleService.sayHelloTo("test");
    }

}
