package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedObject;
import net.tvburger.up.behaviors.Specification;

public interface UpEndpointTechnologyInfo extends Specification, ManagedObject.Info {

    Class<?> getEndpointType();

}
