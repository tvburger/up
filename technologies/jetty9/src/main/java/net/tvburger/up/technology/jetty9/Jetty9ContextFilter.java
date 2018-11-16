package net.tvburger.up.technology.jetty9;

import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.runtime.impl.UpContextHolder;
import net.tvburger.up.runtime.impl.UpContextImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.technology.jsr340.Jsr340;
import net.tvburger.up.topology.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public final class Jetty9ContextFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(Jetty9ContextFilter.class);
    private final Identity identity;
    private final Jsr340.Endpoint endpoint;

    public Jetty9ContextFilter(Identity identity, Jsr340.Endpoint endpoint) {
        this.identity = identity;
        this.endpoint = endpoint;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        UpContext engineContext = UpContextHolder.getContext();
        try {
            UpContextHolder.setContext(UpContextImpl.Factory.createEndpointContext(endpoint, identity, engineContext));
            logger.info("Serving URI: " + ((HttpServletRequest) request).getRequestURI());
            chain.doFilter(request, response);
            logger.info("Returning from: " + ((HttpServletRequest) request).getRequestURI());
        } catch (TopologyException | AccessDeniedException cause) {
            logger.error("Failed to set context: " + cause.getMessage(), cause);
            throw new ServletException("Failed to set context!");
        } catch (ServletException | IOException cause) {
            logger.error("Failure during processing request: " + cause.getMessage(), cause);
            throw cause;
        } finally {
            UpContextHolder.setContext(engineContext);
        }
    }

    @Override
    public void destroy() {
    }

}
