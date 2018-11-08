package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedObject;
import net.tvburger.up.behaviors.Specification;

public interface EndpointTechnologyInfo<T> extends Specification, ManagedObject.Info {

    Class<T> getEndpointType();

}
