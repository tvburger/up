package net.tvburger.up.applications.admin;

import net.tvburger.up.technology.jsr370.Jsr370;
import net.tvburger.up.topology.UpApplicationTopology;

public final class AdminApplicationTopology extends UpApplicationTopology {

    public AdminApplicationTopology() {
        super(new UpApplicationTopology.Builder()
                .withEndpointDefinition(Jsr370.Endpoint.Definition.Factory.create(AdminApplication.class))
                .build());
    }

}
