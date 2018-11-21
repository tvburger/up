package net.tvburger.up.applications.api;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.behaviors.LifecycleException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/")
public final class ApiEnvironmentManager {

    private final UpEnvironment.Manager manager;

    public ApiEnvironmentManager(UpEnvironment.Manager manager) {
        this.manager = manager;
    }

//    @Path("/deploy/package")
//    @POST
//    public UUID deployPackage(InputStream bytes) throws DeployException {
//
//        manager.deployPackage(packageDefinition);
//        try {
//            UpContext context = UpContext.getContext();
//            UpResourceRepository store = context.getRuntime().getFileStore(manager.getInfo());
//            UUID applicationUuid = UUID.randomUUID();
//            store.save(applicationDefinitionStream, applicationUuid);
//            File file = store.resolve(applicationUuid);
//            manager.deploy(ApiApplicationDefinition.Factory.create(file));
//        } catch (AccessDeniedException cause) {
//            throw new DeployException("Failed to deploy application: " + cause.getMessage(), cause);
//        }
//        return null;
//    }

// TODO: support application, serivce and endpoint definitions (?)
//    @Path("/deploy/service")
//    @POST
//    public void deploy(ApiServiceDefinition serviceDefinition, ApiApplicationDefinition applicationDefinition) throws DeployException {
//        try {
//            manager.deploy(serviceDefinition.toUp(), applicationDefinition);
//        } catch (IOException cause) {
//            throw new DeployException("Failed to deploy: " + cause.getMessage(), cause);
//        }
//    }
//
//    @Path("/deploy/endpoint")
//    @POST
//    public void deploy(ApiEndpointDefinition endpointDefinition, ApiApplicationDefinition applicationDefinition) throws DeployException {
//        try {
//            manager.deploy(endpointDefinition.toUp(), applicationDefinition);
//        } catch (IOException cause) {
//            throw new DeployException("Failed to deploy: " + cause.getMessage(), cause);
//        }
//    }

    @Path("/init")
    @POST
    public void init() throws LifecycleException {
        manager.init();
    }

    @Path("/start")
    @POST
    public void start() throws LifecycleException {
        manager.start();
    }

    @Path("/stop")
    @POST
    public void stop() throws LifecycleException {
        manager.stop();
    }

    @Path("/destroy")
    @POST
    public void destroy() throws LifecycleException {
        manager.destroy();
    }

    @Path("/state")
    @GET
    public String getState() {
        return manager.getState().name();
    }

    @Path("/logged")
    @GET
    public boolean isLogged() {
        return manager.isLogged();
    }

    @Path("/logged")
    @POST
    public void setLogged(boolean logged) {
        manager.setLogged(logged);
    }

    @Path("/info")
    @GET
    public UpEnvironment.Info getInfo() {
        return manager.getInfo();
    }

}
