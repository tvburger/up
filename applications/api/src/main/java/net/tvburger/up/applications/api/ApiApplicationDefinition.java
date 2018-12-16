package net.tvburger.up.applications.api;

import net.tvburger.up.applications.api.jaxrs.ApiApplicationApplication;
import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.technology.jsr370.Jsr370;

public final class ApiApplicationDefinition extends UpApplicationDefinition {

    public ApiApplicationDefinition() {
        super("api-application", new UpApplicationDefinition.Builder()
                .withEndpointDefinition(Jsr370.Endpoint.Definition.Factory.create(ApiApplicationApplication.class))
                .build());
    }

}
