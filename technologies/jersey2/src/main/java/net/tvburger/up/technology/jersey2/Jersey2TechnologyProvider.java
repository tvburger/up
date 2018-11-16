package net.tvburger.up.technology.jersey2;

import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.runtime.spi.UpEndpointTechnologyProvider;
import net.tvburger.up.technology.jsr370.Jsr370;

public final class Jersey2TechnologyProvider implements UpEndpointTechnologyProvider {

    @Override
    public Class<?> getEndpointType() {
        return Jsr370.Endpoint.class;
    }

    @Override
    public UpEndpointTechnology<?, ?> getEndpointTechnology() throws UpRuntimeException {
        UpContext context = UpContext.getContext();
        if (context == null || context.getService() != null || context.getEndpoint() != null) {
            throw new UpRuntimeException("Not running inside an UpEngine, or inside a UpService or UpEndpoint!");
        }
        return new Jersey2Technology(new Jersey2TechnologyManager(context.getEngine(), context.getIdentity()));
    }
}
