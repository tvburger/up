package net.tvburger.up.technology.jetty9;

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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;

public final class Jetty9ContextServlet implements Servlet {

    private final UpEngine engine;
    private final Identity identity;
    private final EndpointInfo endpointInfo;
    private final Servlet servlet;

    public Jetty9ContextServlet(UpEngine engine, Identity identity, EndpointInfo endpointInfo, Servlet servlet) {
        this.engine = engine;
        this.identity = identity;
        this.endpointInfo = endpointInfo;
        this.servlet = servlet;
    }

    private UpContext createContext(ServletRequest servletRequest) throws ServletException {
        try {
            String contextPath = ((HttpServletRequest) servletRequest).getContextPath();
            String[] parts = contextPath.split("/");
            if (parts.length != 2 || !parts[0].equals("")) {
                throw new ServletException("Invalid context path: " + contextPath);
            }
            String environmentName = parts[1];
            if (!engine.getRuntime().hasEnvironment(environmentName)) {
                throw new ServletException("Unknown environment: " + environmentName);
            }
            Environment environment = engine.getRuntime().getEnvironment(environmentName);
            UpContextImpl context = new UpContextImpl();
            context.setTransactionInfo(TransactionInfo.Factory.create(URI.create(((HttpServletRequest) servletRequest).getRequestURI())));
            context.setCallerInfo(CallerInfo.Factory.create(endpointInfo));
            context.setLocality(Locality.Factory.create(engine));
            context.setIdentity(identity);
            context.setEnvironment(environment);
            return context;
        } catch (AccessDeniedException cause) {
            throw new ServletException("Access denied");
        }
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
        UpContext context = Up.getContext();
        try {
            Up.setContext(createContext(servletRequest));
            servlet.service(servletRequest, servletResponse);
        } finally {
            Up.setContext(context);
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
