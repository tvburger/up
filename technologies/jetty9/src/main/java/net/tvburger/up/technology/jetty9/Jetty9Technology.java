package net.tvburger.up.technology.jetty9;

import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.jsr340.Jsr340;

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
    public Set<Jsr340.Endpoint> getEndpoints(EnvironmentInfo environmentInfo) throws AccessDeniedException {
        return technologyManager.getEndpoints(environmentInfo);
    }

}
