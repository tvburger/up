package net.tvburger.up.technology.jetty9;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.Identity;
import net.tvburger.up.spi.EndpointTechnologyProvider;

public final class Jetty9TechnologyProvider implements EndpointTechnologyProvider {

    @Override
    public EndpointTechnology<?> getEndpointTechnology(UpEngine engine, Identity identity) {
        return new Jetty9Technology(new Jetty9TechnologyManager(engine, identity));
    }

}
