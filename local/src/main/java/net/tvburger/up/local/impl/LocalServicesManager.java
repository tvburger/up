package net.tvburger.up.local.impl;

import net.tvburger.up.Environment;
import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.Service;
import net.tvburger.up.ServiceManager;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.impl.*;
import net.tvburger.up.logger.UpLogger;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;
import net.tvburger.up.util.Services;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.*;

public final class LocalServicesManager {

    private final Map<Class<?>, Set<Service<?>>> serviceRegistry = new HashMap<>();
    private final Map<Object, Class<?>> serviceIndex = new HashMap<>();
    private final UpEngine engine;
    private final EnvironmentInfo environmentInfo;
    private final UpLogger logger;

    LocalServicesManager(UpEngine engine, EnvironmentInfo environmentInfo, UpLogger logger) {
        this.engine = engine;
        this.environmentInfo = environmentInfo;
        this.logger = logger;
    }

    public <T, S extends T> Service<T> addService(Class<T> serviceType, Class<S> serviceClass, Object[] arguments) throws AccessDeniedException, DeployException {
        if (serviceClass == null || serviceType == null || !serviceType.isAssignableFrom(serviceClass) || !serviceType.isInterface()) {
            throw new DeployException("Invalid serviceType and serviceClass: " + serviceType + " - " + serviceClass);
        }
        Identity identity = Identities.ANONYMOUS;
        ServiceManager<T> serviceManager = createServiceManager(serviceType, serviceClass, identity);
        Environment environment = engine.getRuntime().getEnvironment(environmentInfo.getName());
        T serviceInstance = createLoggerProxy(Services.instantiateService(environment, serviceClass, arguments), identity, serviceManager);
        Set<Service<?>> services = serviceRegistry.computeIfAbsent(serviceType, (key) -> new HashSet<>());
        Service<T> service = new ServiceImpl<>(serviceManager, serviceInstance);
        services.add(service);
        serviceIndex.put(service, serviceType);
        return service;
    }

    private <T, S extends T> ServiceManager<T> createServiceManager(Class<T> serviceType, Class<S> serviceClass, Identity identity) {
        return ServiceManagerImpl.Factory.create(
                ImplementationImpl.Factory.create(
                        SpecificationImpl.Factory.create(serviceType.getName(), getVersion(serviceType)),
                        serviceClass.getName(),
                        getVersion(serviceClass)
                ),
                ServiceInfoImpl.Factory.create(
                        serviceType,
                        Identities.getSafeIdentification(identity),
                        UUID.randomUUID(),
                        environmentInfo));
    }

    private String getVersion(Class<?> clazz) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if ("serialVersionUID".equals(field.getName()) && Modifier.isStatic(field.getModifiers())) {
                    return Objects.toString(field.get(null));
                }
            }
            return "unversioned";
        } catch (IllegalAccessException cause) {
            throw new IllegalStateException(cause);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createLoggerProxy(T service, Identity identity, ServiceManager<T> serviceManager) {
        return (T) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new LocalServiceProxy(engine, new ServiceImpl<>(serviceManager, service), identity, logger));
    }

    public <T> void removeService(Service<T> service) {
        if (serviceIndex.containsKey(service)) {
            serviceRegistry.get(serviceIndex.remove(service)).remove(service);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Service<T> getService(Class<T> serviceType) throws DeployException {
        if (!serviceRegistry.containsKey(serviceType)) {
            throw new DeployException("No such service registered: " + serviceType);
        }
        Iterator<Service<?>> iterator = serviceRegistry.get(serviceType).iterator();
        if (!iterator.hasNext()) {
            throw new IllegalStateException();
        }
        return (Service<T>) iterator.next();
    }

}
