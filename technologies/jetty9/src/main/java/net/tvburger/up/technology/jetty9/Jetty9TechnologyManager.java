package net.tvburger.up.technology.jetty9;

import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.EndpointTechnologyManager;
import net.tvburger.up.Environment;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.definitions.EndpointDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.technology.servlet.JSR340Manager;
import net.tvburger.up.technology.servlet.JSR340TechnologyInfo;
import net.tvburger.up.util.Services;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.List;

// https://www.eclipse.org/jetty/documentation/9.4.x/embedded-examples.html
// TODO: add logging
// TODO: add removal of servlet
// TODO: handle environments (based on host headers)
public final class Jetty9TechnologyManager implements EndpointTechnologyManager<JSR340Manager> {

    private final UpEngine engine;
    private Server server;
    private ServletHandler servletHandler;
    private JSR340Manager JSR340Manager;
    private boolean logged;

    public Jetty9TechnologyManager(UpEngine engine) {
        this.engine = engine;
    }

    public UpEngine getEngine() {
        return engine;
    }

    public JSR340Manager getJSR340Manager(String environmentName) {
        return JSR340Manager;
    }

    @Override
    public void init() {
        servletHandler = new ServletHandler();
        server = new Server(8091);
        server.setStopAtShutdown(true);
        server.setHandler(servletHandler);
        JSR340Manager = new Jetty9JSR340Manager();
    }

    @Override
    public void start() throws LifecycleException {
        try {
            server.start();
        } catch (Exception cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        try {
            server.stop();
        } catch (Exception cause) {
            throw new LifecycleException(cause);
        }
    }

    @Override
    public void destroy() {
        server.destroy();
        server = null;
        servletHandler = null;
    }

    @Override
    public EndpointTechnologyInfo<JSR340Manager> getInfo() {
        return JSR340TechnologyInfo.get();
    }

    @Override
    public String getImplementationName() {
        return Jetty9Implementation.get().getImplementationName();
    }

    @Override
    public String getImplementationVersion() {
        return Jetty9Implementation.get().getImplementationVersion();
    }

    @Override
    public Specification getSpecification() {
        return Jetty9Implementation.get().getSpecification();
    }

    @Override
    public boolean isLogged() {
        return logged;
    }

    @Override
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deploy(String environmentName, EndpointDefinition endpointDefinition, Class<?> serviceClass) throws DeployException, AccessDeniedException {
        if (serviceClass == null || environmentName == null || endpointDefinition == null) {
            throw new IllegalArgumentException();
        }
        if (!endpointDefinition.getEndpointReference().equals(getSpecification())) {
            throw new DeployException("Unsupported specification!");
        }
        if (!Servlet.class.isAssignableFrom(serviceClass)) {
            throw new DeployException("Illegal service class, not a Servlet: " + serviceClass.getName());
        }
        List<Object> arguments = endpointDefinition.getArguments();
        if (arguments.size() != 1 && arguments.get(0) instanceof String) {
            throw new DeployException("Invalid endpoint definition!");
        }
        Environment environment = engine.getRuntime().getEnvironment(environmentName);
        Servlet servlet = Services.instantiateService(environment, (Class<? extends Servlet>) serviceClass, new ArrayList<>(endpointDefinition.getServiceDefinition().getArguments()).toArray());
        String mapping = (String) arguments.get(0);
        String envMapping = "/" + environmentName + (mapping.startsWith("/") ? "" : "/") + arguments.get(0);
        servletHandler.addServletWithMapping(new ServletHolder(servlet), envMapping);
    }

}
