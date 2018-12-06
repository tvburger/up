package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.*;

public final class LocalApplication implements UpApplication {

    public static final class Factory {

        static LocalApplication create(LocalApplicationManager manager, Identification identification) {
            Objects.requireNonNull(manager);
            Objects.requireNonNull(identification);
            return new LocalApplication(manager, identification);
        }

        private Factory() {
        }

    }

    private final Identification identification;
    private final LocalApplicationManager manager;

    private LocalApplication(LocalApplicationManager manager, Identification identification) {
        this.manager = manager;
        this.identification = identification;
    }

    @Override
    public Set<UpService.Info<?>> listServices() {
        return Collections.unmodifiableSet(manager.getServices().keySet());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getService(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return (T) manager.getServices().get(serviceInfo).getInterface();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> UpService.Manager<T> getServiceManager(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return (UpService.Manager<T>) manager.getServices().get(serviceInfo).getManager();
    }

    @Override
    public Map<Specification, Set<? extends UpEndpoint.Info>> listEndpoints() {
        Map<Specification, Set<? extends UpEndpoint.Info>> listedEndpoints = new LinkedHashMap<>();
        for (Map.Entry<Specification, Map<UpEndpoint.Info, UpEndpoint.Manager<?>>> entry : manager.getEndpoints().entrySet()) {
            listedEndpoints.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue().keySet()));
        }
        return Collections.unmodifiableMap(listedEndpoints);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends UpEndpoint.Info> UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException {
        for (Map<UpEndpoint.Info, UpEndpoint.Manager<?>> endpointSet : manager.getEndpoints().values()) {
            if (endpointSet.containsKey(endpointInfo)) {
                return (UpEndpoint.Manager<I>) endpointSet.get(endpointInfo);
            }
        }
        return null;
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public Manager getManager() throws AccessDeniedException {
        return manager;
    }

    @Override
    public Info getInfo() {
        return manager.getInfo();
    }

}
