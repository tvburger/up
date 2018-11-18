package net.tvburger.up.runtimes.local;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.impl.ImplementationImpl;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.impl.UpServiceImpl;
import net.tvburger.up.runtime.impl.UpServiceInfoImpl;
import net.tvburger.up.runtime.impl.UpServiceManagerImpl;
import net.tvburger.up.runtime.util.UpServices;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.util.Identities;

import java.util.*;

public final class LocalServicesManager {

    private final Map<Class<?>, Set<UpService<?>>> serviceRegistry = new HashMap<>();
    private final Set<UpService.Info<?>> services = new HashSet<>();
    private final Map<UpService.Info<?>, UpService<?>> infoServiceIndex = new HashMap<>();
    private final Map<Object, Class<?>> serviceIndex = new HashMap<>();
    private final UpEngine engine;
    private final UpEnvironment.Info environmentInfo;

    LocalServicesManager(UpEngine engine, UpEnvironment.Info environmentInfo) {
        this.engine = engine;
        this.environmentInfo = environmentInfo;
    }

    public <T, S extends T> UpService<T> addService(Class<T> serviceType, Class<S> serviceClass, Object[] arguments) throws AccessDeniedException, TopologyException {
        if (serviceClass == null || serviceType == null || !serviceType.isAssignableFrom(serviceClass) || !serviceType.isInterface()) {
            throw new TopologyException("Invalid serviceType and serviceClass: " + serviceType + " - " + serviceClass);
        }
        Identity identity = Identities.ANONYMOUS;
        UpService.Manager<T> serviceManager = createServiceManager(serviceType, serviceClass, identity);
        UpEnvironment environment = engine.getRuntime().getEnvironment(environmentInfo.getName());
        T serviceInstance = LocalServiceProxy.Factory.create(UpServices.instantiateService(environment, serviceClass, arguments), identity, serviceManager);
        Set<UpService<?>> services = serviceRegistry.computeIfAbsent(serviceType, (key) -> new HashSet<>());
        UpService<T> service = new UpServiceImpl<>(serviceManager, serviceInstance);
        services.add(service);
        this.services.add(service.getInfo());
        infoServiceIndex.put(service.getInfo(), service);
        serviceIndex.put(service, serviceType);
        return service;
    }

    private <T, S extends T> UpService.Manager<T> createServiceManager(Class<T> serviceType, Class<S> serviceClass, Identity identity) {
        return UpServiceManagerImpl.Factory.create(
                ImplementationImpl.Factory.create(serviceType, serviceClass),
                UpServiceInfoImpl.Factory.create(
                        serviceType,
                        Identities.getSafeIdentification(identity),
                        UUID.randomUUID(),
                        environmentInfo));
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
        if (!iterator.hasNext()) {
            return null;
        }
        while (iterator.hasNext()) {
            UpService<?> service = iterator.next();
            try {
                LifecycleManager.State state = service.getManager().getState();
                if (state != LifecycleManager.State.RETIRED && state != LifecycleManager.State.FAILED) {
                    return (UpService<T>) service;
                }
            } catch (AccessDeniedException cause) {
                return null;
            }
        }
        return null;
    }

    public Set<Class<?>> listServiceTypes() {
        return Collections.unmodifiableSet(serviceRegistry.keySet());
    }

    public Set<UpService.Info<?>> listServices() {
        return Collections.unmodifiableSet(services);
    }

}
