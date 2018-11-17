package net.tvburger.up.applications.api;

import net.tvburger.up.technology.jsr370.Jsr370;
import net.tvburger.up.topology.UpApplicationTopology;

public final class ApiApplicationTopology extends UpApplicationTopology {

    public ApiApplicationTopology() {
        super(new UpApplicationTopology.Builder()
                .withEndpointDefinition(Jsr370.Endpoint.Definition.Factory.create(ApiApplication.class))
                .build());
    }

}
