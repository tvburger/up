package net.tvburger.up.technology.jsr340;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.behaviors.impl.SpecificationImpl;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.impl.UpEndpointTechnologyInfoImpl;
import net.tvburger.up.security.Identification;
import net.tvburger.up.topology.InstanceDefinition;
import net.tvburger.up.topology.UpEndpointDefinition;

import javax.servlet.Servlet;
import java.net.URI;
import java.util.*;

public interface Jsr340 extends UpEndpointTechnology<Jsr340.Endpoint, Jsr340.Endpoint.Info> {

    interface Endpoint extends UpEndpoint<Endpoint.Manager, Endpoint.Info> {

        final class Definition extends UpEndpointDefinition {

            public static final class Factory {

                public static Definition createStatic(String mapping, String resourcePath) {
                    return new Builder()
                            .withMapping(mapping)
                            .withServletClass(ResourcesServlet.class)
                            .withInitParameter("resourcePath", resourcePath)
                            .build();
                }

                private Factory() {
                }

            }

            public static Definition parse(UpEndpointDefinition endpointDefinition) throws IllegalArgumentException {
                if (endpointDefinition instanceof Definition) {
                    return (Definition) endpointDefinition;
                }
                if (!Servlet.class.isAssignableFrom(endpointDefinition.getInstanceDefinition().getInstanceClass())) {
                    throw new IllegalArgumentException("ServletClass does not implement Servlet!");
                }
                return new Definition(endpointDefinition.getInstanceDefinition(), endpointDefinition.getSettings());
            }

            private Definition(InstanceDefinition instanceDefinition, Map<String, String> settings) {
                super(Specification.get(), instanceDefinition, settings);
            }

            public String getMapping() {
                return getSettings().getOrDefault("mapping", "/*");
            }

            public Map<String, String> getInitParameters() {
                Map<String, String> initParameters = new LinkedHashMap<>();
                for (Map.Entry<String, String> entry : getSettings().entrySet()) {
                    if (entry.getKey().startsWith("init.")) {
                        initParameters.put(entry.getKey().substring("init.".length()), entry.getValue());
                    }
                }
                return initParameters;
            }

            @SuppressWarnings("unchecked")
            public Class<? extends Servlet> getServletClass() {
                return (Class<? extends Servlet>) getInstanceDefinition().getInstanceClass();
            }

            public List<Object> getArguments() {
                return getInstanceDefinition().getArguments();
            }

            public final static class Builder {
                private Class<? extends Servlet> servletClass;
                private String mapping;
                private final Map<String, String> initParameters = new LinkedHashMap<>();
                private final ArrayList<Object> arguments = new ArrayList<>();

                public Builder withServletInstance(Class<? extends Servlet> servletClass, Object... arguments) {
                    this.servletClass = servletClass;
                    if (arguments != null) {
                        this.arguments.addAll(Arrays.asList(arguments));
                    }
                    return this;
                }

                public Builder withServletClass(Class<? extends Servlet> servletClass) {
                    this.servletClass = servletClass;
                    arguments.clear();
                    return this;
                }

                public Builder withMapping(String mapping) {
                    this.mapping = mapping;
                    return this;
                }

                public Builder withInitParameter(String name, String value) {
                    initParameters.put(name, value);
                    return this;
                }

                public Definition build() {
                    if (servletClass == null) {
                        throw new IllegalStateException();
                    }
                    UpEndpointDefinition.Builder builder = new UpEndpointDefinition.Builder()
                            .withEndpointTechnology(Specification.get())
                            .withEndpointDefinition(InstanceDefinition.Factory.create(servletClass, arguments.toArray()))
                            .withSetting("mapping", mapping == null ? "/*" : mapping);
                    Map<String, String> settings = new LinkedHashMap<>();
                    for (Map.Entry<String, String> entry : initParameters.entrySet()) {
                        builder.withSetting("init." + entry.getKey(), entry.getValue());
                    }
                    return Definition.parse(builder.build());
                }
            }

        }

        interface Manager extends UpEndpoint.Manager<Info> {
        }

        final class Info implements UpEndpoint.Info {

            private final URI endpointUri;
            private final Identification identification;
            private final Class<? extends Servlet> servletClass;
            private final int port;
            private final String serverName;
            private final String contextPath;
            private final String mapping;
            private final String name;
            private final UpEnvironment.Info environmentInfo;

            public Info(URI endpointUri, Identification identification, Class<? extends Servlet> servletClass, int port, String serverName, String contextPath, String mapping, String name, UpEnvironment.Info environmentInfo) {
                this.endpointUri = endpointUri;
                this.identification = identification;
                this.servletClass = servletClass;
                this.port = port;
                this.serverName = serverName;
                this.contextPath = contextPath;
                this.mapping = mapping;
                this.name = name;
                this.environmentInfo = environmentInfo;
            }

            @Override
            public URI getEndpointUri() {
                return endpointUri;
            }

            @Override
            public Identification getIdentification() {
                return identification;
            }

            public Class<? extends Servlet> getServletClass() {
                return servletClass;
            }

            public int getPort() {
                return port;
            }

            public String getServerName() {
                return serverName;
            }

            public String getContextPath() {
                return contextPath;
            }

            public String getMapping() {
                return mapping;
            }

            public String getName() {
                return name;
            }

            @Override
            public UpEndpointTechnologyInfo getEndpointTechnologyInfo() {
                return Jsr340.Info.get();
            }

            @Override
            public UpEnvironment.Info getEnvironmentInfo() {
                return environmentInfo;
            }

            @Override
            public String toString() {
                return String.format("Jsr340.UpEndpoint.Info{%s, %s}", name, endpointUri);
            }

        }

    }

    final class Specification extends SpecificationImpl {

        private static final Specification specification = new Specification();

        public static Specification get() {
            return specification;
        }

        private Specification() {
            super("Servlet", "3.1");
        }

    }

    final class Info extends UpEndpointTechnologyInfoImpl {

        private static final Info info = new Info();

        public static Info get() {
            return info;
        }

        private Info() {
            super(Endpoint.class, Specification.get());
        }

    }

    interface Manager extends UpEndpointTechnology.Manager<Endpoint> {
    }

}
