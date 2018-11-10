package net.tvburger.up.technology.jetty9;

import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jsr340.Jsr340;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.Servlet;

public final class Jetty9Endpoint implements Jsr340.Endpoint {

    public static final class Factory {

        public static Jetty9Endpoint create(Class<? extends Servlet> servletClass, ServletContextHandler context, ServerConnector http, String mapping) {
            int port = http.getLocalPort();
            String serverName = http.getHost();
            String contextPath = context.getContextPath();
            String name = servletClass.getName();
            String url = "http://" + serverName + (port != 80 ? ":" + port : "") + contextPath + mapping;
            return new Jetty9Endpoint(new Info(url, port, serverName, contextPath, mapping, name));
        }

        private Factory() {
        }

    }

    public static final class Info implements Jsr340.Endpoint.Info {

        private final String url;
        private final int port;
        private final String serverName;
        private final String contextPath;
        private final String mapping;
        private final String name;

        public Info(String url, int port, String serverName, String contextPath, String mapping, String name) {
            this.url = url;
            this.port = port;
            this.serverName = serverName;
            this.contextPath = contextPath;
            this.mapping = mapping;
            this.name = name;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public int getPort() {
            return port;
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
            return String.format("Jetty2Endpoint.Info{%s, %s}", name, url);
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
