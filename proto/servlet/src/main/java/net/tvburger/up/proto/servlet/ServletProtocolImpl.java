package net.tvburger.up.proto.servlet;

import net.tvburger.up.deploy.EndpointTechnology;
import net.tvburger.up.impl.ServiceUtil;
import net.tvburger.up.proto.ServletProtocolManager;
import net.tvburger.up.spi.ProtocolLifecycleManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.io.IOException;

// https://www.eclipse.org/jetty/documentation/9.4.x/embedded-examples.html
// TODO: add logging
// TODO: add removal of servlet
// TODO: handle environments (based on host headers)
public class ServletProtocolImpl implements ServletProtocolManager, ProtocolLifecycleManager<ServletProtocolManager> {

    public static final EndpointTechnology TECHNOLOGY = new EndpointTechnology("servlet", "3.1");

    private Server server;
    private ServletHandler servletHandler;

    @Override
    public void registerServlet(String mapping, Class<? extends Servlet> servletClass, Object... arguments) {
        Servlet servlet = ServiceUtil.instantiateService(servletClass, arguments);
        servletHandler.addServletWithMapping(new ServletHolder(servlet), mapping);
    }

    @Override
    public EndpointTechnology getEndpointTechnology() {
        return TECHNOLOGY;
    }

    @Override
    public Class<ServletProtocolManager> getProtocolType() {
        return ServletProtocolManager.class;
    }

    @Override
    public ServletProtocolManager getProtocolManager() {
        return this;
    }

    @Override
    public void init() {
        servletHandler = new ServletHandler();
        server = new Server(8099);
        server.setStopAtShutdown(true);
        server.setHandler(servletHandler);
    }

    @Override
    public void start() throws IOException {
        try {
            server.start();
        } catch (Exception cause) {
            throw new IOException(cause);
        }
    }

    @Override
    public void stop() throws IOException {
        try {
            server.stop();
        } catch (Exception cause) {
            throw new IOException(cause);
        }
    }

    @Override
    public void destroy() {
        server.destroy();
    }

}
