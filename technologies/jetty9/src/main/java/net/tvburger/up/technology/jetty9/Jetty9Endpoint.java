package net.tvburger.up.technology.jetty9;

import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;
import net.tvburger.up.technology.jsr340.Jsr340;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Jetty9Endpoint implements Jsr340.Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(Jetty9Endpoint.class);

    public static final class Factory {

        public static Jetty9Endpoint create(Info endpointInfo, Jetty9TechnologyManager technologyManager) {
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

        @Override
        public void start() throws LifecycleException {
            logger.info("Starting");
            super.start();
            manager.restartIfNeeded();
        }

        @Override
        public void stop() throws LifecycleException {
            logger.info("Stopping");
            super.stop();
            manager.restartIfNeeded();
        }

        @Override
        public void destroy() throws LifecycleException {
            logger.info("Destroying");
            super.destroy();
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

    public Jetty9Endpoint(Manager manager, Identification identification) {
        this.manager = manager;
        this.identification = identification;
    }

    @Override
    public Jsr340.Endpoint.Manager getManager() throws AccessDeniedException {
        return manager;
    }

    @Override
    public Jsr340.Endpoint.Info getInfo() {
        return manager.getInfo();
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

}
