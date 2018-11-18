package net.tvburger.up.technology.jersey2;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.runtime.impl.UpContextHolder;
import net.tvburger.up.runtime.impl.UpContextImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.topology.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class Jersey2ContextApplication extends Application {

    @Priority(Integer.MIN_VALUE)
    public final class BeforeFilter implements ContainerRequestFilter {

        @Override
        public void filter(ContainerRequestContext containerRequestContext) throws IOException {
            try {
                UpContext engineContext = UpContextHolder.getContext();
                contexts.set(engineContext);
                UpContextHolder.setContext(UpContextImpl.Factory.createEndpointContext(endpoint, identity, engineContext));
                logger.info("Serving URI: " + containerRequestContext.getUriInfo().getRequestUri());
            } catch (TopologyException | AccessDeniedException cause) {
                logger.error("Failed to set context: " + cause.getMessage(), cause);
                throw new IOException("Failed to process request");
            }
        }

    }

    @Priority(Integer.MAX_VALUE)
    public final class AfterFilter implements WriterInterceptor {

        @Context
        private UriInfo uriInfo;

        @Override
        public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
            try {
                context.proceed();
            } finally {
                logger.info("Returning from: " + uriInfo.getRequestUri());
                UpContextHolder.setContext(contexts.get());
            }
        }

    }

    private final static Logger logger = LoggerFactory.getLogger(Jersey2ContextApplication.class);
    private final ThreadLocal<UpContext> contexts = new ThreadLocal<>();
    private final Application application;
    private final UpEndpoint endpoint;
    private final Identity identity;

    public Jersey2ContextApplication(Application application, UpEndpoint endpoint, Identity identity) {
        this.application = application;
        this.endpoint = endpoint;
        this.identity = identity;
    }

    public Set<Class<?>> getClasses() {
        return application.getClasses();
    }

    public Set<Object> getSingletons() {
        Set<Object> singletons = new LinkedHashSet<>();
        singletons.add(new BeforeFilter());
        singletons.addAll(application.getSingletons());
        singletons.add(new AfterFilter());
        return singletons;
    }

    public Map<String, Object> getProperties() {
        return application.getProperties();
    }

}
