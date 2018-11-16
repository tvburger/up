package net.tvburger.up.technology.jetty9;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jsr340.Jsr340;
import net.tvburger.up.util.Java8Specification;

import java.util.Set;

public final class Jetty9Technology implements Jsr340 {

    private final Jetty9TechnologyManager technologyManager;

    Jetty9Technology(Jetty9TechnologyManager technologyManager) {
        this.technologyManager = technologyManager;
    }

    @Override
    public Manager getManager() {
        return technologyManager;
    }

    @Override
    public Info getInfo() {
        return technologyManager.getInfo();
    }

    @Override
    public net.tvburger.up.behaviors.Specification getEngineRequirement() {
        return Java8Specification.get();
    }

    @Override
    public Set<Endpoint.Info> listEndpoints(UpEnvironment.Info environmentInfo) {
        return technologyManager.listEndpoints(environmentInfo);
    }

    @Override
    public UpEndpoint.Manager<Endpoint.Info> getEndpointManager(Endpoint.Info endpointInfo) throws AccessDeniedException {
        return technologyManager.getEndpointManager(endpointInfo);
    }

}
