package net.tvburger.up.proto.jaxrs;

import net.tvburger.up.deploy.EndpointTechnology;
import net.tvburger.up.proto.JaxrsProtocolManager;
import net.tvburger.up.spi.ProtocolLifecycleManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

public class JaxrsProtocolImpl implements JaxrsProtocolManager, ProtocolLifecycleManager {

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
    }

    @Override
    public void start() throws IOException {
        // register jaxrs servlet
    }

    @Override
    public void stop() throws IOException {
        // unregister jaxrs servlet
    }

    @Override
    public void destroy() {
        // destroy jaxrs servlet
    }

    @Override
    public <T> void registerResourceClass(Class<T> resourceClass) {
        // add resource to jaxrs servlet
        throw new NotImplementedException();
    }

    @Override
    public <T> void registerResourceSingleton(Class<T> resourceClass, Object... arguments) {
        // add resource to jaxrs servlet
        throw new NotImplementedException();
    }

}
