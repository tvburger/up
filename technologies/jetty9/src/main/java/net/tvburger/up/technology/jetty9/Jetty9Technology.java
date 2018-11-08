package net.tvburger.up.technology.jetty9;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.EndpointTechnologyManager;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.servlet.JSR340Manager;

public final class Jetty9Technology implements EndpointTechnology<JSR340Manager> {

    private final Jetty9TechnologyManager technologyManager;

    public Jetty9Technology(Jetty9TechnologyManager technologyManager) {
        this.technologyManager = technologyManager;
    }

    @Override
    public EndpointTechnologyManager<JSR340Manager> getManager() {
        return technologyManager;
    }

    @Override
    public EndpointTechnologyInfo<JSR340Manager> getInfo() {
        return technologyManager.getInfo();
    }

    @Override
    public JSR340Manager getEndpointManager(String environmentName) throws AccessDeniedException {
        return technologyManager.getJSR340Manager(environmentName);
    }

}
