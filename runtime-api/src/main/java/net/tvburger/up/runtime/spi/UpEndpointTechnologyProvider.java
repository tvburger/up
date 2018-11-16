package net.tvburger.up.runtime.spi;

import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpRuntimeException;

public interface UpEndpointTechnologyProvider {

    Class<?> getEndpointType();

    UpEndpointTechnology<?, ?> getEndpointTechnology() throws UpRuntimeException;

}
