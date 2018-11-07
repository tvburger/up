package net.tvburger.up.proto.jaxrs;

import net.tvburger.up.deploy.EndpointTechnology;
import net.tvburger.up.proto.JaxrsProtocolManager;
import net.tvburger.up.spi.ProtocolLifecycleManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

public class JaxrsProtocolImpl implements JaxrsProtocolManager, ProtocolLifecycleManager {

    private Server server;
    private ServletContextHandler context;
    private ServletHolder servletHolder;

    public static final EndpointTechnology TECHNOLOGY = new EndpointTechnology("jaxrs", "2.1");

    @Override
    public EndpointTechnology getEndpointTechnology() {
        return TECHNOLOGY;
    }

    @Override
    public Class<JaxrsProtocolManager> getProtocolType() {
        return JaxrsProtocolManager.class;
    }

    @Override
    public JaxrsProtocolManager getProtocolManager() {
        return this;
    }

    @Override
    public void init() {
        server = new Server(8088);
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
    }

    @Override
    public void start() throws IOException {
        // register jaxrs servlet
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws IOException {
        // unregister jaxrs servlet
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        // destroy jaxrs servlet
        server.destroy();
    }

    @Override
    public <T> void registerResourceClass(Class<T> resourceClass) {
        // add resource to jaxrs servlet
        throw new NotImplementedException();
    }

    @Override
    public <T> void registerResourceSingleton(Class<T> resourceClass, String pathSpec, Object... arguments) {
        // Add resource to jaxrs servlet
        servletHolder = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, pathSpec);
        servletHolder.setInitParameter(
                "jersey.config.server.provider.classnames",
                resourceClass.getCanonicalName());
    }
}
