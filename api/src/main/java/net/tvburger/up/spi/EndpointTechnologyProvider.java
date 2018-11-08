package net.tvburger.up.spi;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.Identity;

public interface EndpointTechnologyProvider {

    EndpointTechnology<?> getEndpointTechnology(UpEngine engine, Identity engineIdentity) throws DeployException;

}
