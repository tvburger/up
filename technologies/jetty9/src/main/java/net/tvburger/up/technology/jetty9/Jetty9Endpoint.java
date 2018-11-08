package net.tvburger.up.technology.jetty9;

import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jsr340.Jsr340;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.Servlet;

public final class Jetty9Endpoint implements Jsr340.Endpoint {

    public static final class Factory {

        public static Jetty9Endpoint create(Servlet servlet, ServletContextHandler context, ServerConnector http, String mapping) {
            String serverName = http.getHost();
            String contextPath = context.getContextPath();
            String name = servlet.getClass().getName();
            return new Jetty9Endpoint(new Info(serverName, contextPath, mapping, name));
        }

        private Factory() {
        }

    }

    public static final class Info implements Jsr340.Endpoint.Info {

        private final String serverName;
        private final String contextPath;
        private final String mapping;
        private final String name;

        public Info(String serverName, String contextPath, String mapping, String name) {
            this.serverName = serverName;
            this.contextPath = contextPath;
            this.mapping = mapping;
            this.name = name;
        }

        @Override
        public String getServerName() {
            return serverName;
        }

        @Override
        public String getContextPath() {
            return contextPath;
        }

        @Override
        public String getMapping() {
            return mapping;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return String.format("Jetty2Endpoint.Info{%s, %s, %s, %s}", serverName, contextPath, mapping, name);
        }

    }

    private final Info info;

    private Jetty9Endpoint(Info info) {
        this.info = info;
    }

    @Override
    public Manager getManager() throws AccessDeniedException {
        return null;
    }

    @Override
    public Info getInfo() {
        return info;
    }

}
