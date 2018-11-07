package net.tvburger.up.example;

import net.tvburger.up.Up;
import net.tvburger.up.UpClient;
import net.tvburger.up.proto.JaxrsProtocolManager;
import net.tvburger.up.proto.ServletProtocolManager;

public class Example {

    private static void jaxrsDemo() throws InterruptedException {
        UpClient client = Up.createClient();
        client.getProtocol(JaxrsProtocolManager.class).registerResourceSingleton( ExampleResource.class,"/*");
    }

    private static void servletDemo() throws InterruptedException {
        UpClient client = Up.createClient();

        client.addService(DependencyServiceImpl.class).getManager().setLogged(true);
        client.addService(ExampleServiceImpl.class, "Hello", DependencyService.class).getManager().setLogged(true);

        client.getProtocol(ServletProtocolManager.class).registerServlet("/*", ExampleServlet.class, ExampleService.class);

        Thread.sleep(1_000_000);
    }

    public static void main(String[] args) throws InterruptedException {
        //servletDemo();
        jaxrsDemo();
    }


}
