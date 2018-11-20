package net.tvburger.up.technology.jersey2;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jsr370.Jsr370;
import net.tvburger.up.util.Java8Specification;

import java.util.Set;

public final class Jersey2Technology implements UpEndpointTechnology<Jsr370.Endpoint.Info> {

    private final Jersey2TechnologyManager technologyManager;

    public Jersey2Technology(Jersey2TechnologyManager technologyManager) {
        this.technologyManager = technologyManager;
    }

    @Override
    public Jersey2TechnologyManager getManager() {
        return technologyManager;
    }

    @Override
    public Jsr370.Info getInfo() {
        return technologyManager.getInfo();
    }

    @Override
    public net.tvburger.up.behaviors.Specification getEngineRequirement() {
        return Java8Specification.get();
    }

    @Override
    public Set<Jsr370.Endpoint.Info> listEndpoints(UpEnvironment.Info environmentInfo) {
        return technologyManager.listServices(environmentInfo);
    }

    @Override
    public UpEndpoint.Manager<Jsr370.Endpoint.Info> getEndpointManager(Jsr370.Endpoint.Info endpointInfo) throws AccessDeniedException {
        return technologyManager.getEndpointManager(endpointInfo);
    }

}
