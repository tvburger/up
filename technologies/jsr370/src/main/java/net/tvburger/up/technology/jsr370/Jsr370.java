package net.tvburger.up.technology.jsr370;

import net.tvburger.up.*;
import net.tvburger.up.impl.EndpointTechnologyInfoImpl;
import net.tvburger.up.impl.SpecificationImpl;
import net.tvburger.up.topology.EndpointDefinition;
import net.tvburger.up.topology.InstanceDefinition;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface Jsr370 extends EndpointTechnology<Jsr370.Endpoint> {

    interface Endpoint extends net.tvburger.up.Endpoint<Endpoint.Manager, Endpoint.Info> {

        class Definition extends EndpointDefinition {

            public static final class Factory {

                public static Definition create(Class<? extends Application> applicationType) {
                    Objects.requireNonNull(applicationType);
                    for (Class<?> clazz = applicationType; !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
                        ApplicationPath path = clazz.getAnnotation(ApplicationPath.class);
                        if (path != null) {
                            return create(path.value(), applicationType);
                        }
                    }
                    throw new IllegalArgumentException("No ApplicationPath found, use explicit mapping!");
                }

                public static Definition create(String mapping, Class<? extends Application> applicationType) {
                    Objects.requireNonNull(applicationType);
                    Map<String, String> map = new HashMap<>();
                    return new Definition(
                            InstanceDefinition.Factory.create(applicationType),
                            Collections.singletonMap("mapping", mapping));
                }

                private Factory() {
                }

            }

            public static Definition parse(EndpointDefinition endpointDefinition) throws IllegalArgumentException {
                if (endpointDefinition instanceof Definition) {
                    return (Definition) endpointDefinition;
                }
                if (!"application".equals(endpointDefinition.getSettings().get("type"))) {
                    throw new IllegalArgumentException("Type is not application!");
                }
                if (!Application.class.isAssignableFrom(endpointDefinition.getInstanceDefinition().getInstanceClass())) {
                    throw new IllegalArgumentException("InstanceClass does not extend Application!");
                }
                return new Definition(endpointDefinition.getInstanceDefinition(), endpointDefinition.getSettings());
            }

            private Definition(InstanceDefinition instanceDefinition, Map<String, String> settings) {
                super(Specification.get(), instanceDefinition, settings);
            }

            @SuppressWarnings("unchecked")
            public Class<? extends Application> getApplicationClass() {
                return (Class<? extends Application>) getInstanceDefinition().getInstanceClass();
            }

            public String getMapping() {
                return getSettings().getOrDefault("mapping", "/");
            }

        }

        interface Manager extends EndpointManager<Info> {
        }

        final class Info implements EndpointInfo {

            private final String url;
            private final Class<? extends Application> applicationClass;
            private final int port;
            private final String serverName;
            private final String contextPath;
            private final String mapping;
            private final String name;
            private final EnvironmentInfo environmentInfo;

            public Info(String url, Class<? extends Application> applicationClass, int port, String serverName, String contextPath, String mapping, String name, EnvironmentInfo environmentInfo) {
                this.url = url;
                this.applicationClass = applicationClass;
                this.port = port;
                this.serverName = serverName;
                this.contextPath = contextPath;
                this.mapping = mapping;
                this.name = name;
                this.environmentInfo = environmentInfo;
            }

            public String getUrl() {
                return url;
            }

            public Class<? extends Application> getApplicationClass() {
                return applicationClass;
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

            public EnvironmentInfo getEnvironmentInfo() {
                return environmentInfo;
            }

            public String toString() {
                return String.format("Jsr340.Endpoint.Info{%s, %s}", name, url);
            }

        }

    }

    final class Specification extends SpecificationImpl {

        private static final Specification specification = new Specification();

        public static Specification get() {
            return specification;
        }

        private Specification() {
            super("JAX-RS", "2.1");
        }

    }

    final class Info extends EndpointTechnologyInfoImpl<Endpoint> {

        private static final Info info = new Info();

        public static Info get() {
            return info;
        }

        private Info() {
            super(Endpoint.class, Specification.get());
        }

    }

    interface Manager extends EndpointTechnologyManager<Endpoint> {
    }

}
