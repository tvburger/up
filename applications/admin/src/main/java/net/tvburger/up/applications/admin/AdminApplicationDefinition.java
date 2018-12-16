package net.tvburger.up.applications.admin;

import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.technology.jsr370.Jsr370;

public final class AdminApplicationDefinition extends UpApplicationDefinition {

    public AdminApplicationDefinition() {
        super("admin", new UpApplicationDefinition.Builder()
                .withEndpointDefinition(Jsr370.Endpoint.Definition.Factory.create(AdminApplication.class))
                .build());
    }

}
