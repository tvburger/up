package net.tvburger.up.applications.client;

import net.tvburger.up.technology.jsr370.Jsr370;
import net.tvburger.up.topology.UpApplicationTopology;

public final class ClientApplicationTopology extends UpApplicationTopology {

    public ClientApplicationTopology() {
        super(new UpApplicationTopology.Builder()
                .withEndpointDefinition(Jsr370.Endpoint.Definition.Factory.create(ClientApplication.class))
                .build());
    }

}
