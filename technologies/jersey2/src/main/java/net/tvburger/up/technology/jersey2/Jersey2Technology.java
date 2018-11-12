package net.tvburger.up.technology.jersey2;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.EndpointTechnologyManager;
import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jsr370.Jsr370;

import java.util.Collections;
import java.util.Set;

public final class Jersey2Technology implements EndpointTechnology<Jsr370.Endpoint> {

    private final Jersey2TechnologyManager technologyManager;

    public Jersey2Technology(Jersey2TechnologyManager technologyManager) {
        this.technologyManager = technologyManager;
    }

    @Override
    public EndpointTechnologyManager<Jsr370.Endpoint> getManager() {
        return technologyManager;
    }

    @Override
    public EndpointTechnologyInfo<Jsr370.Endpoint> getInfo() {
        return technologyManager.getInfo();
    }

    @Override
    public Set<Jsr370.Endpoint> getEndpoints(EnvironmentInfo environmentInfo) throws AccessDeniedException {
        return technologyManager.getServices(environmentInfo);
    }

}
