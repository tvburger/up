package net.tvburger.up.technology.jersey2;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.Identity;
import net.tvburger.up.spi.EndpointTechnologyProvider;

public final class Jersey2TechnologyProvider implements EndpointTechnologyProvider {

    @Override
    public EndpointTechnology<?> getEndpointTechnology(UpEngine engine, Identity identity) {
        return new Jersey2Technology(new Jersey2TechnologyManager(engine, identity));
    }

}
