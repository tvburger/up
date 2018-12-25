package net.tvburger.up.technology.jersey2;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.impl.LifecycleManagerImpl;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;
import net.tvburger.up.technology.jsr370.Jsr370;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class Jersey2Endpoint implements Jsr370.Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(Jersey2Endpoint.class);

    public static final class Factory {

        static Jersey2Endpoint create(Jsr370.Endpoint.Info endpointInfo, Jersey2TechnologyManager technologyManager) {
            Objects.requireNonNull(endpointInfo);
            Objects.requireNonNull(technologyManager);
            logger.info("Creating new endpoint: " + endpointInfo);
            return new Jersey2Endpoint(new Manager(endpointInfo, technologyManager), endpointInfo.getIdentification());
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

        void doInit() throws LifecycleException {
            logger.info("Initializing jersey2 endpoint: " + info.getEndpointUri());
            super.init();
        }

        void doStart() throws LifecycleException {
            logger.info("Starting jersey2 endpoint: " + info.getEndpointUri());
            super.start();
        }

        void doStop() throws LifecycleException {
            logger.info("Stopping jersey2 endpoint: " + info.getEndpointUri());
            super.stop();
        }

        void doDestroy() throws LifecycleException {
            logger.info("Destroying jersey2 endpoint: " + info.getEndpointUri());
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
        public Jersey2Endpoint.Info getInfo() {
            return info;
        }

    }

    private final Manager manager;
    private final Identification identification;

    private Jersey2Endpoint(Manager manager, Identification identification) {
        this.manager = manager;
        this.identification = identification;
    }

    @Override
    public Jersey2Endpoint.Manager getManager() throws AccessDeniedException {
        return manager;
    }

    @Override
    public Jersey2Endpoint.Info getInfo() {
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
