package net.tvburger.up.example;

import net.tvburger.up.Up;
import net.tvburger.up.UpClient;
import net.tvburger.up.proto.ServletProtocolManager;

public class Example {

    public static void main(String[] args) throws InterruptedException {
        UpClient client = Up.createClient();
        client.addService(DependencyServiceImpl.class);
        client.addService(ExampleServiceImpl.class, "Hello", DependencyService.class);
        client.getServiceManager(client.getService(DependencyService.class)).setLogged(true);
        ExampleService exampleService = client.getService(ExampleService.class);
        client.getServiceManager(exampleService).setLogged(true);

        client.getProtocol(ServletProtocolManager.class).registerServlet("/*", ExampleServlet.class, ExampleService.class);
        Thread.sleep(1_000_000);
    }

}
