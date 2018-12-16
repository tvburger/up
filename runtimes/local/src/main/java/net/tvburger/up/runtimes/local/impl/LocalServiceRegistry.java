package net.tvburger.up.runtimes.local.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpPackage;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.impl.ImplementationImpl;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpServiceDefinition;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.impl.UpServiceImpl;
import net.tvburger.up.runtime.impl.UpServiceInfoImpl;
import net.tvburger.up.runtime.impl.UpServiceManagerImpl;
import net.tvburger.up.runtime.util.UpServices;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public final class LocalServiceRegistry {

    private final Map<Class<?>, Set<UpService<?>>> serviceRegistry = new ConcurrentHashMap<>();
    private final Map<UpApplication.Info, Set<UpService.Info<?>>> services = new ConcurrentHashMap<>();
    private final Map<UpService.Info<?>, UpService<?>> infoServiceIndex = new ConcurrentHashMap<>();
    private final Map<Object, Class<?>> serviceIndex = new ConcurrentHashMap<>();

    private final UpEngine engine;
    private final UpEnvironment.Info environmentInfo;

    LocalServiceRegistry(UpEngine engine, UpEnvironment.Info environmentInfo) {
        this.engine = engine;
        this.environmentInfo = environmentInfo;
    }

    @SuppressWarnings("unchecked")
    public <T, S extends T> UpService<T> deployService(UpServiceDefinition serviceDefinition, UpApplication application, UpPackage upPackage) throws AccessDeniedException, DeployException {
        Objects.requireNonNull(serviceDefinition);
        Objects.requireNonNull(application);
        Objects.requireNonNull(upPackage);
        try {
            Class<T> serviceType = (Class<T>) upPackage.getClassLoader().loadClass(serviceDefinition.getServiceType());
            Class<S> serviceClass = (Class<S>) upPackage.getClassLoader().loadClass(serviceDefinition.getInstanceDefinition().getClassSpecification());
            if (!serviceType.isAssignableFrom(serviceClass) || !serviceType.isInterface()) {
                throw new DeployException("Invalid serviceType and serviceClass: " + serviceType + " - " + serviceClass);
            }
            Identity identity = Identities.createAnonymous();
            UpService.Manager<T> serviceManager = createServiceManager(serviceType, serviceClass, identity, application.getInfo());
            UpEnvironment environment = engine.getRuntime().getEnvironment(environmentInfo.getName());
            Object[] arguments = new ArrayList<>(serviceDefinition.getInstanceDefinition().getArguments()).toArray();
            T serviceInstance = LocalServiceProxy.Factory.create(
                    UpServices.instantiateService(environment, serviceClass, arguments),
                    upPackage,
                    application,
                    identity,
                    serviceManager);
            Set<UpService<?>> services = serviceRegistry.computeIfAbsent(serviceType, (key) -> new CopyOnWriteArraySet<>());
            UpService<T> service = new UpServiceImpl<>(serviceManager, serviceInstance);
            services.add(service);
            this.services.computeIfAbsent(application.getInfo(), k -> new CopyOnWriteArraySet<>()).add(service.getInfo());
            infoServiceIndex.put(service.getInfo(), service);
            serviceIndex.put(service, serviceType);
            return service;
        } catch (ClassNotFoundException | ClassCastException cause) {
            throw new DeployException(cause);
        }
    }

    private <T, S extends T> UpService.Manager<T> createServiceManager(Class<T> serviceType, Class<S> serviceClass, Identity identity, UpApplication.Info applicationInfo) {
        return UpServiceManagerImpl.Factory.create(
                ImplementationImpl.Factory.create(serviceType, serviceClass),
                UpServiceInfoImpl.Factory.create(
                        serviceType,
                        Identities.getSafeIdentification(identity),
                        UUID.randomUUID(),
                        applicationInfo));
    }

    public <T> void removeService(UpService<T> service) {
        if (serviceIndex.containsKey(service)) {
            serviceRegistry.get(serviceIndex.remove(service)).remove(service);
            infoServiceIndex.remove(service.getInfo());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> UpService<T> getService(UpService.Info<T> serviceInfo) {
        return (UpService<T>) infoServiceIndex.get(serviceInfo);
    }

    @SuppressWarnings("unchecked")
    public <T> UpService<T> getService(Class<T> serviceType) {
        if (!serviceRegistry.containsKey(serviceType)) {
            return null;
        }
        Iterator<UpService<?>> iterator = serviceRegistry.get(serviceType).iterator();
        while (iterator.hasNext()) {
            UpService<?> service = iterator.next();
            try {
                LifecycleManager.State state = service.getManager().getState();
                if (state != LifecycleManager.State.RETIRED && state != LifecycleManager.State.FAILED) {
                    return (UpService<T>) service;
                }
            } catch (AccessDeniedException cause) {
            }
        }
        return null;
    }

    public Set<Class<?>> listServiceTypes() {
        return Collections.unmodifiableSet(serviceRegistry.keySet());
    }

    public Map<UpApplication.Info, Set<UpService.Info<?>>> listServices() {
        Map<UpApplication.Info, Set<UpService.Info<?>>> services = new HashMap<>();
        for (Map.Entry<UpApplication.Info, Set<UpService.Info<?>>> entry : this.services.entrySet()) {
            services.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
        }
        return Collections.unmodifiableMap(services);
    }

}
