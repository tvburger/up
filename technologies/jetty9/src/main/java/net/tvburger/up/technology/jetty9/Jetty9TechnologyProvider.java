package net.tvburger.up.technology.jetty9;

import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.runtime.spi.UpEndpointTechnologyProvider;
import net.tvburger.up.technology.jsr340.Jsr340;

public final class Jetty9TechnologyProvider implements UpEndpointTechnologyProvider {

    @Override
    public Class<?> getEndpointType() {
        return Jsr340.Endpoint.class;
    }

    @Override
    public UpEndpointTechnology<?, ?> getEndpointTechnology() throws UpRuntimeException {
        UpContext context = UpContext.getContext();
        if (context == null || context.getService() != null || context.getEndpoint() != null) {
            throw new UpRuntimeException("Not running inside an UpEngine, or inside a UpService or UpEndpoint!");
        }
        return new Jetty9Technology(new Jetty9TechnologyManager(context.getEngine(), context.getIdentity()));
    }

}
