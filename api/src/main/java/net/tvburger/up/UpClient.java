package net.tvburger.up;

import net.tvburger.up.identity.Entity;
import net.tvburger.up.spi.ProtocolManager;

public interface UpClient extends Entity {

    Environment getEnvironment();

    <T> Service<T> getService(Class<T> serviceType);

    @SuppressWarnings("unchecked")
    default <T, S extends T> Service<T> addService(Class<S> serviceClass, Object... arguments) {
        if (serviceClass == null || serviceClass.getInterfaces().length == 0) {
            throw new IllegalArgumentException();
        }
        return registerService((Class<T>) serviceClass.getInterfaces()[0], serviceClass, arguments);
    }

    <T, S extends T> Service<T> registerService(Class<T> serviceType, Class<S> serviceClass, Object... arguments);

    <P extends ProtocolManager> boolean supportsProtocol(Class<P> protocolType);

    <P extends ProtocolManager> P getProtocol(Class<P> protocolType);

}
