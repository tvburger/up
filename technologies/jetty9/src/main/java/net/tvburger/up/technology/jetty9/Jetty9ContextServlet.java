package net.tvburger.up.technology.jetty9;

import net.tvburger.up.Up;
import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.UpContext;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.impl.UpContextImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public final class Jetty9ContextServlet implements Servlet {

    private final UpEngine engine;
    private final Identity identity;
    private final Servlet servlet;

    public Jetty9ContextServlet(UpEngine engine, Identity identity, Servlet servlet) {
        this.engine = engine;
        this.identity = identity;
        this.servlet = servlet;
    }

    private UpContext createContext(ServletRequest servletRequest) throws IOException {
        try {
            String[] parts = ((HttpServletRequest) servletRequest).getPathInfo().split("/");
            if (parts.length < 2 || !parts[0].equals("")) {
                throw new IOException("Invalid request!");
            }
            String environmentName = parts[1];
            UpContextImpl context = new UpContextImpl();
            context.setCallerInfo(CallerInfo.Factory.create());
            context.setLocality(Locality.Factory.create(engine));
            context.setIdentity(identity);
            context.setEnvironment(engine.getRuntime().getEnvironment(environmentName));
            return context;
        } catch (AccessDeniedException cause) {
            throw new IOException(cause);
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
