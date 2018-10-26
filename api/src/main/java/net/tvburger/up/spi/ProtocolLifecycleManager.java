package net.tvburger.up.spi;

import net.tvburger.up.UpClient;

import java.io.IOException;

public interface ProtocolLifecycleManager<P extends ProtocolManager> {

    Class<P> getProtocolType();

    P getProtocolManager();

    void init(UpClient upClient) throws IOException;

    void start() throws IOException;

    void stop() throws IOException;

    void destroy() throws IOException;

}
