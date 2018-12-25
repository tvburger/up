package net.tvburger.up.applications.api.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.runtime.context.UpContext;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;

@Path("/")
public final class ApiRoot {

    @Path("/")
    public ApiEnvironment getEnvironment() {
        UpEnvironment environment = UpContext.getContext().getEnvironment();
        if (environment == null) {
            throw new NotFoundException();
        }
        return new ApiEnvironment(environment);
    }

}
