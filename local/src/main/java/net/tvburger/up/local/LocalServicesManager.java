package net.tvburger.up.local;

import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.Service;
import net.tvburger.up.admin.ServiceManager;
import net.tvburger.up.identity.Identity;
import net.tvburger.up.impl.ServiceImpl;
import net.tvburger.up.impl.ServiceInfoImpl;
import net.tvburger.up.impl.ServiceManagerImpl;
import net.tvburger.up.impl.ServiceUtil;
import net.tvburger.up.logger.Logger;

import java.lang.reflect.Proxy;
import java.util.*;

public class LocalServicesManager {

    private final Map<Class<?>, Set<Service<?>>> serviceRegistry = new HashMap<>();
    private final Map<Object, Class<?>> serviceIndex = new HashMap<>();
    private final EnvironmentInfo environmentInfo;
    private final Logger logger;

    public LocalServicesManager(EnvironmentInfo environmentInfo, Logger logger) {
        this.environmentInfo = environmentInfo;
        this.logger = logger;
    }

    public <T, S extends T> Service<T> addService(Class<T> serviceType, Class<S> serviceClass, Object[] arguments) {
        if (serviceClass == null || serviceType == null || !serviceType.isAssignableFrom(serviceClass) || !serviceType.isInterface()) {
            throw new IllegalArgumentException();
        }
        ServiceManager<T> serviceManager = createServiceManager(serviceType);
        T serviceInstance = createLoggerProxy(ServiceUtil.instantiateService(serviceClass, arguments), serviceManager);
        Set<Service<?>> services = serviceRegistry.computeIfAbsent(serviceType, (key) -> new HashSet<>());
        Service<T> service = new ServiceImpl<>(serviceManager, serviceInstance);
        services.add(service);
        serviceIndex.put(service, serviceType);
        return service;
    }

    private <T> ServiceManager<T> createServiceManager(Class<T> serviceClass) {
        return new ServiceManagerImpl<>(new ServiceInfoImpl<>(
                serviceClass,
                Identity.ANONYMOUS,
                UUID.randomUUID(),
                environmentInfo));
    }

    @SuppressWarnings("unchecked")
    private <T> T createLoggerProxy(T service, ServiceManager<T> serviceManager) {
        return (T) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new LocalServiceProxy(new ServiceImpl<>(serviceManager, service), logger));
    }

    public <T> void removeService(Service<T> service) {
        if (serviceIndex.containsKey(service)) {
            serviceRegistry.get(serviceIndex.remove(service)).remove(service);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Service<T> getService(Class<T> serviceType) {
        if (!serviceRegistry.containsKey(serviceType)) {
            throw new IllegalStateException();
        }
        Iterator<Service<?>> iterator = serviceRegistry.get(serviceType).iterator();
        if (!iterator.hasNext()) {
            throw new IllegalStateException();
        }
        return (Service<T>) iterator.next();
    }

}
