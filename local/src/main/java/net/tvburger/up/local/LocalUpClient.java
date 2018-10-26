package net.tvburger.up.local;

import net.tvburger.up.ServiceInfo;
import net.tvburger.up.UpClient;
import net.tvburger.up.admin.ServiceManager;
import net.tvburger.up.impl.ProtocolLifecycleManagerProvider;
import net.tvburger.up.spi.ProtocolManager;

public class LocalUpClient implements UpClient {

    private final LocalEnvironmentManager environmentManager;
    private final ProtocolLifecycleManagerProvider protocolProvider;

    public LocalUpClient(LocalEnvironmentManager environmentManager, ProtocolLifecycleManagerProvider protocolProvider) {
        this.environmentManager = environmentManager;
        this.protocolProvider = protocolProvider;
    }

    @Override
    public String getEnvironment() {
        return environmentManager.getEnvironment();
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
    public <T, S extends T> void addTypedService(Class<T> serviceType, Class<S> serviceClass, Object... arguments) {
        environmentManager.getLocalServicesManager().addService(serviceType, serviceClass, arguments);
    }

    @Override
    public <T> void removeService(T service) {
        environmentManager.getLocalServicesManager().removeService(service);
    }

    @Override
    public <P extends ProtocolManager> boolean supportsProtocol(Class<P> protocolType) {
        return protocolProvider.getProtocols().contains(protocolType);
    }

    @Override
    public <P extends ProtocolManager> P getProtocol(Class<P> protocolType) {
        return protocolProvider.get(protocolType).getProtocolManager();
    }

}
