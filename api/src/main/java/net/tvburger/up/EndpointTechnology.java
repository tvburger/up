package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedObject;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Set;

public interface EndpointTechnology<T> extends ManagedObject<EndpointTechnologyManager<T>, EndpointTechnologyInfo<T>> {

    Set<T> getEndpoints(EnvironmentInfo environmentInfo) throws AccessDeniedException, DeployException;

}
