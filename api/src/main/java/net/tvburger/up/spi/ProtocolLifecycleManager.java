package net.tvburger.up.spi;

import net.tvburger.up.deploy.EndpointTechnology;

import java.io.IOException;

public interface ProtocolLifecycleManager<P extends ProtocolManager> {

    EndpointTechnology getEndpointTechnology();

    Class<P> getProtocolType();

    P getProtocolManager();

    void init() throws IOException;

    void start() throws IOException;

    void stop() throws IOException;

    void destroy() throws IOException;

}
