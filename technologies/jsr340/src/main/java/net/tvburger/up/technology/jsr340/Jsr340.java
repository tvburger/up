package net.tvburger.up.technology.jsr340;

import net.tvburger.up.EndpointInfo;
import net.tvburger.up.EndpointManager;
import net.tvburger.up.impl.EndpointTechnologyInfoImpl;
import net.tvburger.up.impl.SpecificationImpl;

public interface Jsr340 {

    interface Endpoint extends net.tvburger.up.Endpoint<Endpoint.Manager, Endpoint.Info> {

        interface Manager extends EndpointManager<Info> {
        }

        interface Info extends EndpointInfo {

            String getUrl();

            int getPort();

            String getServerName();

            String getContextPath();

            String getMapping();

            String getName();

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

    final class TechnologyInfo extends EndpointTechnologyInfoImpl<Endpoint> {

        private static final TechnologyInfo info = new TechnologyInfo();

        public static TechnologyInfo get() {
            return info;
        }

        private TechnologyInfo() {
            super(Endpoint.class, Specification.get());
        }

    }
}
