package net.tvburger.up.technology.jetty9;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;
import net.tvburger.up.technology.jsr340.Jsr340;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class Jetty9Endpoint implements Jsr340.Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(Jetty9Endpoint.class);

    public static final class Factory {

        static Jetty9Endpoint create(Info endpointInfo, Jetty9TechnologyManager technologyManager) {
            Objects.requireNonNull(endpointInfo);
            Objects.requireNonNull(technologyManager);
            logger.info("Creating new endpoint: " + endpointInfo);
            return new Jetty9Endpoint(new Manager(endpointInfo, technologyManager), endpointInfo.getIdentification());
        }

        private Factory() {
        }

    }

    static final class Manager extends LifecycleManagerImpl implements Jsr340.Endpoint.Manager {

        private final Info info;
        private final Jetty9TechnologyManager manager;
        private boolean logged;

        public Manager(Info info, Jetty9TechnologyManager manager) {
            this.info = info;
            this.manager = manager;
        }

        void doInit() throws LifecycleException {
            logger.info("Initializing jetty9 endpoint: " + info.getEndpointUri());
            super.init();
        }

        void doStart() throws LifecycleException {
            logger.info("Starting jetty9 endpoint: " + info.getEndpointUri());
            super.start();
        }

        void doStop() throws LifecycleException {
            logger.info("Stopping jetty9 endpoint: " + info.getEndpointUri());
            super.stop();
        }

        void doDestroy() throws LifecycleException {
            logger.info("Destroying jetty9 endpoint: " + info.getEndpointUri());
            super.destroy();
        }

        @Override
        public void start() throws LifecycleException {
            logger.info("Starting");
            manager.start(info);
        }

        @Override
        public void stop() throws LifecycleException {
            logger.info("Stopping");
            manager.stop(info);
        }

        @Override
        public void destroy() throws LifecycleException {
            logger.info("Destroying");
            manager.destroy(info);
        }

        @Override
        public boolean isLogged() {
            return logged;
        }

        @Override
        public void setLogged(boolean logged) {
            this.logged = logged;
        }

        @Override
        public Jsr340.Endpoint.Info getInfo() {
            return info;
        }

    }

    private final Manager manager;
    private final Identification identification;

    private Jetty9Endpoint(Manager manager, Identification identification) {
        this.manager = manager;
        this.identification = identification;
    }

    @Override
    public Jetty9Endpoint.Manager getManager() throws AccessDeniedException {
        return manager;
    }

    @Override
    public Jetty9Endpoint.Info getInfo() {
        return manager.getInfo();
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInfo()) * 3 + 7;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof UpEndpoint)) {
            return false;
        }
        return Objects.equals(getInfo(), ((UpEndpoint<?, ?>) object).getInfo());
    }

}
