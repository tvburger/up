package net.tvburger.up.spi;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;

public interface EndpointTechnologyProvider {

    EndpointTechnology<?> getEndpointTechnology(UpEngine engine) throws DeployException;

}
