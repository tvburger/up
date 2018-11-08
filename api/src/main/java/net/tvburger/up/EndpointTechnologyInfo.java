package net.tvburger.up;

import net.tvburger.up.behaviors.Specification;

public interface EndpointTechnologyInfo<T> extends Specification {

    Class<T> getEndpointManagerType();

}
