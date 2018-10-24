package net.tvburger.up;

import java.util.Set;

public interface JaxrsProtocolManager extends ProtocolManager {

    void registerResource(Class<?> resourceType, Object... args);

    void unregisterResource(Object resource);

    Set<Object> getResources();

}
