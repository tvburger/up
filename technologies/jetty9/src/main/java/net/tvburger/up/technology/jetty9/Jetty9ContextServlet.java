package net.tvburger.up.technology.jetty9;

import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.runtime.impl.UpContextHolder;
import net.tvburger.up.runtime.impl.UpContextImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.technology.jsr340.Jsr340;
import net.tvburger.up.topology.TopologyException;

import javax.servlet.*;
import java.io.IOException;

public final class Jetty9ContextServlet implements Servlet {

    private final Identity identity;
    private final Jsr340.Endpoint endpoint;
    private final Servlet servlet;

    public Jetty9ContextServlet(Identity identity, Jsr340.Endpoint endpoint, Servlet servlet) {
        this.identity = identity;
        this.endpoint = endpoint;
        this.servlet = servlet;
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        servlet.init(servletConfig);
    }

    @Override
    public ServletConfig getServletConfig() {
        return servlet.getServletConfig();
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        UpContext engineContext = UpContextHolder.getContext();
        try {
            UpContextHolder.setContext(UpContextImpl.Factory.createEndpointContext(endpoint, identity, engineContext));
            servlet.service(servletRequest, servletResponse);
        } catch (TopologyException | AccessDeniedException cause) {
            throw new ServletException("Failed to set context!");
        } finally {
            UpContextHolder.setContext(engineContext);
        }
    }

    @Override
    public String getServletInfo() {
        return servlet.getServletInfo();
    }

    @Override
    public void destroy() {
        servlet.destroy();
    }

}
