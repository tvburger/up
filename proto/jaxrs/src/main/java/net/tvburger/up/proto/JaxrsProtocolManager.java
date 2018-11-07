package net.tvburger.up.proto;

import net.tvburger.up.spi.ProtocolManager;

public interface JaxrsProtocolManager extends ProtocolManager {

    <T> void registerResourceClass(Class<T> resourceClass);

    <T> void registerResourceSingleton(Class<T> resourceClass, String pathSpec, Object... arguments);

}
