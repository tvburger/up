package net.tvburger.up.applications.api;

import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.technology.jsr370.Jsr370;

public final class ApiDefinition extends UpApplicationDefinition {

    public ApiDefinition() {
        super("api-application", new UpApplicationDefinition.Builder()
                .withEndpointDefinition(Jsr370.Endpoint.Definition.Factory.create(ApiApplicationApplication.class))
                .build());
    }

}
