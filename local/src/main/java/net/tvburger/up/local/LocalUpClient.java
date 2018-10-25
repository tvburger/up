package net.tvburger.up.local;

import net.tvburger.up.ServiceInfo;
import net.tvburger.up.ServiceManager;
import net.tvburger.up.UpClient;

public class LocalUpClient implements UpClient {

    private final LocalEnvironmentManager environmentManager;

    public LocalUpClient(LocalEnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    @Override
    public <T> T getService(Class<T> serviceType) {
        return environmentManager.getLocalServicesManager().getService(serviceType);
    }

    @Override
    public <T> ServiceInfo<T> getServiceInfo(T service) {
        return getServiceManager(service).getServiceInfo();
    }

    @Override
    public <T> ServiceManager<T> getServiceManager(T service) {
        return environmentManager.getLocalServicesManager().getServiceManager(service);
    }

    @Override
    public <T> void removeService(T service) {
        environmentManager.getLocalServicesManager().removeService(service);
    }

    @Override
    public <T> void addService(Class<T> serviceClass, Object... arguments) {
        environmentManager.getLocalServicesManager().addService(serviceClass, arguments);
    }

}
