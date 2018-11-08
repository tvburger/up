package net.tvburger.up.technology.jersey2;

import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.EndpointTechnologyManager;
import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.definitions.EndpointDefinition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.technology.jsr370.Jsr370;

public final class Jersey2TechnologyManager implements EndpointTechnologyManager<Jsr370.Endpoint> {

    private final UpEngine engine;
    private final Identity identity;
    //    private Server server;
//    private ServletHandler servletHandler;
    private boolean logged;

    public Jersey2TechnologyManager(UpEngine engine, Identity identity) {
        this.engine = engine;
        this.identity = identity;
    }

    public UpEngine getEngine() {
        return engine;
    }

    @Override
    public void init() {
//        servletHandler = new ServletHandler();
//        server = new Server(8091);
//        server.setStopAtShutdown(true);
//        server.setHandler(servletHandler);
//        jsr370Manager = new Jersey2Jsr370Manager();
    }

    @Override
    public void start() throws LifecycleException {
//        try {
//            server.start();
//        } catch (Exception cause) {
//            throw new LifecycleException(cause);
//        }
    }

    @Override
    public void stop() throws LifecycleException {
//        try {
//            server.stop();
//        } catch (Exception cause) {
//            throw new LifecycleException(cause);
//        }
    }

    @Override
    public void destroy() {
//        server.destroy();
//        server = null;
//        servletHandler = null;
    }

    @Override
    public EndpointTechnologyInfo<Jsr370.Endpoint> getInfo() {
        return Jsr370.TechnologyInfo.get();
    }

    @Override
    public String getImplementationName() {
        return Jersey2Implementation.get().getImplementationName();
    }

    @Override
    public String getImplementationVersion() {
        return Jersey2Implementation.get().getImplementationVersion();
    }

    @Override
    public Specification getSpecification() {
        return Jersey2Implementation.get().getSpecification();
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
    public void deploy(EnvironmentInfo environmentInfo, EndpointDefinition endpointDefinition) throws DeployException, AccessDeniedException {
//        if (environmentInfo == null || endpointDefinition == null) {
//            throw new IllegalArgumentException();
//        }
//        if (!endpointDefinition.getEndpointTechnology().equals(getSpecification())) {
//            throw new DeployException("Unsupported specification!");
//        }
//        Class<?> serviceClass = endpointDefinition.getServiceDefinition().getServiceImplementation();
//        if (!Application.class.isAssignableFrom(serviceClass)) {
//            throw new DeployException("Illegal service class, not an Application: " + serviceClass.getName());
//        }
//        List<Object> arguments = endpointDefinition.getArguments();
//        if (arguments.size() != 1 && arguments.get(0) instanceof String) {
//            throw new DeployException("Invalid endpoint definition!");
//        }
//        Environment environment = engine.getRuntime().getEnvironment(environmentInfo.getName());
//        Servlet servlet = Services.instantiateService(environment, (Class<? extends Servlet>) serviceClass, new ArrayList<>(endpointDefinition.getServiceDefinition().getArguments()).toArray());
//        String mapping = (String) arguments.get(0);
//        String envMapping = "/" + environmentInfo.getName() + (mapping.startsWith("/") ? "" : "/") + arguments.get(0);
//        servletHandler.addServletWithMapping(new ServletHolder(new Jersey2ContextServlet(engine, identity, servlet)), envMapping);
    }

}
