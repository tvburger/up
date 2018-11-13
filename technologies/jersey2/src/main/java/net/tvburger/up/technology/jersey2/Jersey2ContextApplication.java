package net.tvburger.up.technology.jersey2;

import net.tvburger.up.EndpointInfo;
import net.tvburger.up.Environment;
import net.tvburger.up.Up;
import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.TransactionInfo;
import net.tvburger.up.context.UpContext;
import net.tvburger.up.impl.UpContextImpl;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class Jersey2ContextApplication extends Application {

    @PreMatching
    @Priority(Integer.MIN_VALUE)
    public final class BeforeFilter implements ContainerRequestFilter {

        @Override
        public void filter(ContainerRequestContext containerRequestContext) throws IOException {
            logger.info("Entering before filter for: " + containerRequestContext.getUriInfo().getRequestUri());
            UpContext context = Up.getContext();
            contexts.set(context);
            Up.setContext(createContext(context, containerRequestContext));
        }

    }

    @Priority(Integer.MAX_VALUE)
    public final class AfterFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
            logger.info("Entering after filter for: " + containerRequestContext.getUriInfo().getRequestUri());
            Up.setContext(contexts.get());
        }

    }

    private final static Logger logger = LoggerFactory.getLogger(Jersey2ContextApplication.class);
    private final ThreadLocal<UpContext> contexts = new ThreadLocal<>();
    private final Application application;
    private final EndpointInfo endpointInfo;
    private final UpEngine engine;
    private final Identity identity;

    public Jersey2ContextApplication(Application application, EndpointInfo endpointInfo, UpEngine engine, Identity identity) {
        this.application = application;
        this.endpointInfo = endpointInfo;
        this.engine = engine;
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

    private UpContext createContext(UpContext context, ContainerRequestContext containerRequestContext) throws IOException {
        try {
            String requestPath = containerRequestContext.getUriInfo().getRequestUri().getPath();
            String[] parts = requestPath.split("/");
            if (parts.length < 2 || !parts[0].equals("")) {
                throw new IOException("Invalid request path: " + requestPath);
            }
            String environmentName = parts[1];
            if (!engine.getRuntime().hasEnvironment(environmentName)) {
                throw new IOException("Unknown environment: " + environmentName);
            }
            Environment environment = engine.getRuntime().getEnvironment(environmentName);
            UpContextImpl newContext = new UpContextImpl();
            newContext.setTransactionInfo(TransactionInfo.Factory.create(containerRequestContext.getUriInfo().getRequestUri()));
            newContext.setCallerInfo(CallerInfo.Factory.create(endpointInfo));
            newContext.setLocality(Locality.Factory.create(engine));
            newContext.setIdentity(identity);
            newContext.setEnvironment(environment);
            return newContext;
        } catch (AccessDeniedException cause) {
            throw new IOException("Access denied");
        }
    }

}
