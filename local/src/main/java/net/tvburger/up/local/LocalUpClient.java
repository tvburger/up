package net.tvburger.up.local;

import net.tvburger.up.Environment;
import net.tvburger.up.Service;
import net.tvburger.up.UpClient;
import net.tvburger.up.identity.Identity;
import net.tvburger.up.impl.ProtocolLifecycleManagerProvider;
import net.tvburger.up.spi.ProtocolManager;

public class LocalUpClient implements UpClient {

    private final LocalEnvironmentManager environmentManager;
    private final ProtocolLifecycleManagerProvider protocolProvider;
    private final Identity identity;

    public LocalUpClient(LocalEnvironmentManager environmentManager, ProtocolLifecycleManagerProvider protocolProvider, Identity identity) {
        this.environmentManager = environmentManager;
        this.protocolProvider = protocolProvider;
        this.identity = identity;
    }

    @Override
    public Environment getEnvironment() {
        return environmentManager.getEnvironment();
    }

    @Override
    public <T> Service<T> getService(Class<T> serviceType) {
        return environmentManager.getLocalServicesManager().getService(serviceType);
    }

    @Override
    public <T, S extends T> Service<T> registerService(Class<T> serviceType, Class<S> serviceClass, Object... arguments) {
        return environmentManager.getLocalServicesManager().addService(serviceType, serviceClass, arguments);
    }

    @Override
    public <P extends ProtocolManager> boolean supportsProtocol(Class<P> protocolType) {
        return protocolProvider.getProtocols().contains(protocolType);
    }

    @Override
    public <P extends ProtocolManager> P getProtocol(Class<P> protocolType) {
        return protocolProvider.get(protocolType).getProtocolManager();
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }
}
