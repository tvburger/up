package net.tvburger.up.technology.jsr370;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.behaviors.impl.SpecificationImpl;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.impl.UpEndpointTechnologyInfoImpl;
import net.tvburger.up.security.Identification;
import net.tvburger.up.topology.InstanceDefinition;
import net.tvburger.up.topology.UpEndpointDefinition;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface Jsr370 extends UpEndpointTechnology<Jsr370.Endpoint, Jsr370.Endpoint.Info> {

    interface Endpoint extends UpEndpoint<Endpoint.Manager, Endpoint.Info> {

        class Definition extends UpEndpointDefinition {

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

            public static Definition parse(UpEndpointDefinition endpointDefinition) throws IllegalArgumentException {
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

        interface Manager extends UpEndpoint.Manager<Info> {
        }

        final class Info implements UpEndpoint.Info {

            private final URI endpointUri;
            private final Identification identification;
            private final Class<? extends Application> applicationClass;
            private final int port;
            private final String serverName;
            private final String contextPath;
            private final String mapping;
            private final String name;
            private final UpEnvironment.Info environmentInfo;

            public Info(URI endpointUri, Identification identification, Class<? extends Application> applicationClass, int port, String serverName, String contextPath, String mapping, String name, UpEnvironment.Info environmentInfo) {
                this.endpointUri = endpointUri;
                this.identification = identification;
                this.applicationClass = applicationClass;
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

            public UpEnvironment.Info getEnvironmentInfo() {
                return environmentInfo;
            }

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
            super("JAX-RS", "2.1");
        }

    }

    final class Info extends UpEndpointTechnologyInfoImpl<Endpoint> {

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
