package net.tvburger.up.applications.api;

import net.tvburger.up.UpException;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.topology.TopologyException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public final class ApiExceptionMapper implements ExceptionMapper<UpException> {

    @Override
    public Response toResponse(UpException exception) {
        Response response;
        if (exception instanceof AccessDeniedException) {
            response = Response.status(Response.Status.UNAUTHORIZED).build();
        } else if (exception instanceof TopologyException) {
            response = Response.status(Response.Status.FORBIDDEN).build();
        } else if (exception instanceof LifecycleException) {
            response = Response.status(Response.Status.CONFLICT).build();
        } else {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

}
