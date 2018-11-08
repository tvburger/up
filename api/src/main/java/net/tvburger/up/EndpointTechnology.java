package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedObject;
import net.tvburger.up.security.AccessDeniedException;

public interface EndpointTechnology<T> extends ManagedObject<EndpointTechnologyManager<T>, EndpointTechnologyInfo<T>> {

    T getEndpointManager(String environmentName) throws AccessDeniedException;

}
