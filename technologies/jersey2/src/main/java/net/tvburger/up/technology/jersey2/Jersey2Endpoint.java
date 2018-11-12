package net.tvburger.up.technology.jersey2;

import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.impl.LifecycleManagerImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jsr370.Jsr370;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Jersey2Endpoint implements Jsr370.Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(Jersey2Endpoint.class);

    public static final class Factory {

        public static Jersey2Endpoint create(Jsr370.Endpoint.Info endpointInfo, Jersey2TechnologyManager technologyManager) {
            logger.info("Creating new endpoint: " + endpointInfo);
            return new Jersey2Endpoint(new Manager(endpointInfo, technologyManager));
        }

        private Factory() {
        }

    }

    static final class Manager extends LifecycleManagerImpl implements Jsr370.Endpoint.Manager {

        private final Jsr370.Endpoint.Info info;
        private final Jersey2TechnologyManager manager;
        private boolean logged;

        public Manager(Jsr370.Endpoint.Info info, Jersey2TechnologyManager manager) {
            this.info = info;
            this.manager = manager;
        }

        @Override
        public void start() throws LifecycleException {
            logger.info("Starting");
            super.start();
            manager.start(info);
        }

        @Override
        public void stop() throws LifecycleException {
            logger.info("Stopping");
            super.stop();
            manager.stop(info);
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
        public Jsr370.Endpoint.Info getInfo() {
            return info;
        }

    }

    private final Manager manager;

    public Jersey2Endpoint(Manager manager) {
        this.manager = manager;
    }

    @Override
    public Jsr370.Endpoint.Manager getManager() throws AccessDeniedException {
        return manager;
    }

    @Override
    public Jsr370.Endpoint.Info getInfo() {
        return manager.getInfo();
    }

}
