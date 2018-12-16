package net.tvburger.up.applications.api.jaxrs;

import net.tvburger.up.UpException;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public final class ApiExceptionMapper implements ExceptionMapper<Throwable> {

    private final Logger logger = LoggerFactory.getLogger(ApiExceptionMapper.class);

    @Override
    public Response toResponse(Throwable throwable) {
        Response response;
        logger.warn("We got exception: " + throwable.getMessage(), throwable);
        if (throwable instanceof UpException) {
            logger.warn("UpException thrown: " + throwable.getMessage(), throwable);
            if (throwable instanceof AccessDeniedException) {
                response = Response.status(Response.Status.UNAUTHORIZED).build();
            } else if (throwable instanceof DeployException) {
                response = Response.status(Response.Status.FORBIDDEN).build();
            } else if (throwable instanceof LifecycleException) {
                response = Response.status(Response.Status.CONFLICT).build();
            } else {
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            if (throwable instanceof NotFoundException) {
                response = Response.status(Response.Status.NOT_FOUND).build();
            } else {
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            logger.error("Exception thrown: " + throwable.getMessage(), throwable);
        }
        return response;
    }

}
