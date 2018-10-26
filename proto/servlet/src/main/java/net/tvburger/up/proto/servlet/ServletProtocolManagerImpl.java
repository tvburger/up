package net.tvburger.up.proto.servlet;

import net.tvburger.up.UpClient;
import net.tvburger.up.proto.ServletProtocolManager;
import net.tvburger.up.spi.ProtocolLifecycleManager;
import org.eclipse.jetty.server.Server;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.Servlet;
import java.io.IOException;

public class ServletProtocolManagerImpl implements ServletProtocolManager, ProtocolLifecycleManager<ServletProtocolManager> {

    private UpClient upClient;
    private Server server;

    @Override
    public void registerServlet(Servlet servlet) {
        // Add servlet
        throw new NotImplementedException();
    }

    @Override
    public void unregisterServlet(Servlet servlet) {
        // Remove servlet
        throw new NotImplementedException();
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
    public void init(UpClient upClient) {
        this.upClient = upClient;
        server = new Server(8080);
        server.setStopAtShutdown(true);
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
