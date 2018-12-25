package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.UpEndpointTechnology;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identification;

import java.util.*;

public final class LocalEnvironment implements UpEnvironment {

    public static final class Factory {

        public static LocalEnvironment create(UpEngine engine, String environmentName, UpRuntimeInfo runtimeInfo) {
            Objects.requireNonNull(engine);
            Objects.requireNonNull(environmentName);
            Objects.requireNonNull(runtimeInfo);
            return new LocalEnvironment(engine, LocalEnvironmentManager.Factory.create(engine, environmentName, runtimeInfo));
        }

        private Factory() {
        }

    }

    private final UpEngine engine;
    private final LocalEnvironmentManager manager;

    private LocalEnvironment(UpEngine engine, LocalEnvironmentManager manager) {
        this.engine = engine;
        this.manager = manager;
    }

    @Override
    public UpEnvironment.Manager getManager() {
        return manager;
    }

    @Override
    public UpEnvironment.Info getInfo() {
        return manager.getInfo();
    }

    @Override
    public Identification getIdentification() {
        return manager.getInfo().getIdentification();
    }

    @Override
    public Set<Class<?>> listServiceTypes() {
        return manager.getServiceRegistry().listServiceTypes();
    }

    @Override
    public <T> T lookupService(Class<T> serviceType) {
        UpService<T> service = manager.getServiceRegistry().getService(serviceType);
        return service == null ? null : service.getInterface();
    }

    @Override
    public Set<UpService.Info<?>> listServices() {
        Set<UpService.Info<?>> services = new HashSet<>();
        for (Set<UpService.Info<?>> serviceInfos : manager.getServiceRegistry().listServices().values()) {
            services.addAll(serviceInfos);
        }
        return services;
    }

    @Override
    public <T> T getService(UpService.Info<T> serviceInfo) {
        return manager.getServiceRegistry().getService(serviceInfo).getInterface();
    }

    @Override
    public <T> UpService.Manager<T> getServiceManager(UpService.Info<T> serviceInfo) throws AccessDeniedException {
        return manager.getServiceRegistry().getService(serviceInfo).getManager();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Specification, Set<? extends UpEndpoint.Info>> listEndpoints() {
        Map<Specification, Set<? extends UpEndpoint.Info>> endpoints = new HashMap<>();
        for (Class<?> endpointType : engine.listEndpointTypes()) {
            UpEndpointTechnology<?> technology = engine.getEndpointTechnology(endpointType);
            Set<? extends UpEndpoint.Info> technologyEndpoints = endpoints.computeIfAbsent(technology.getInfo(), (key) -> new HashSet<>());
            for (Map.Entry entry : technology.listEndpoints(getInfo()).entrySet()) {
                technologyEndpoints.addAll((Set) entry.getValue());
            }
        }
        return endpoints;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends UpEndpoint.Info> UpEndpoint.Manager<I> getEndpointManager(I endpointInfo) throws AccessDeniedException {
        for (Class<?> endpointType : engine.listEndpointTypes()) {
            try {
                UpEndpointTechnology technology = engine.getEndpointTechnology(endpointType);
                UpEndpoint.Manager manager = technology.getEndpointManager(endpointInfo);
                if (manager != null) {
                    return manager;
                }
            } catch (ClassCastException cause) {
            }
        }
        return null;
    }

    @Override
    public Set<UpPackage.Info> listPackages() {
        return Collections.unmodifiableSet(manager.getPackages().keySet());
    }

    @Override
    public UpPackage.Manager getPackageManager(UpPackage.Info packageInfo) throws AccessDeniedException {
        return manager.getPackages().get(packageInfo).getManager();
    }

    @Override
    public Set<UpApplication.Info> listApplications() {
        return Collections.unmodifiableSet(manager.getApplications().keySet());
    }

    @Override
    public UpApplication getApplication(UpApplication.Info applicationInfo) throws AccessDeniedException {
        return manager.getApplications().get(applicationInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInfo()) * 3 + 7;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof UpEnvironment)) {
            return false;
        }
        return Objects.equals(getInfo(), ((UpEnvironment) object).getInfo());
    }

}
