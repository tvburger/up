package net.tvburger.up.local;

import net.tvburger.up.admin.ServiceManager;
import net.tvburger.up.impl.ServiceInfoImpl;
import net.tvburger.up.impl.ServiceManagerImpl;
import net.tvburger.up.logger.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.*;

public class LocalServicesManager {

    private final Map<Class<?>, Set<Object>> serviceRegistry = new HashMap<>();
    private final Map<Object, Class<?>> serviceIndex = new HashMap<>();
    private final String environment;
    private final Logger logger;

    public LocalServicesManager(String environment, Logger logger) {
        this.environment = environment;
        this.logger = logger;
    }

    public <T, S extends T> void addService(Class<T> serviceType, Class<S> serviceClass, Object[] arguments) {
        if (serviceClass == null || serviceType == null || !serviceType.isAssignableFrom(serviceClass) || !serviceType.isInterface()) {
            throw new IllegalArgumentException();
        }
        T service = createLoggerProxy(instantiateService(serviceClass, arguments), createServiceManager(serviceType));
        Set<Object> services = serviceRegistry.computeIfAbsent(serviceType, (key) -> new HashSet<>());
        services.add(service);
        serviceIndex.put(service, serviceType);
    }

    private <T> ServiceManager<T> createServiceManager(Class<T> serviceClass) {
        return new ServiceManagerImpl<>(new ServiceInfoImpl<>(
                environment,
                serviceClass,
                null,
                UUID.randomUUID()));
    }

    @SuppressWarnings("unchecked")
    private <T> T createLoggerProxy(T service, ServiceManager<T> serviceManager) {
        return (T) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new LocalServiceProxy(service, serviceManager, logger));
    }

    @SuppressWarnings("unchecked")
    private <T, S extends T> Class<T> getServiceType(Class<S> serviceClass) {
        Class<?>[] interfaces = serviceClass.getInterfaces();
        if (interfaces.length == 0) {
            throw new IllegalArgumentException("Must implement at least 1 interface!");
        }
        return (Class<T>) interfaces[0];
    }

    @SuppressWarnings("unchecked")
    private <T, S extends T> T instantiateService(Class<S> serviceClass, Object[] arguments) {
        try {
            Constructor<S> constructor = (Constructor<S>) getConstructor(serviceClass, arguments);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0; i < arguments.length; i++) {
                System.out.println(arguments[i]);
                if (parameterTypes[i].equals(arguments[i]) && !parameterTypes[i].equals(Class.class)) {
                    arguments[i] = getService(parameterTypes[i]);
                }
            }
            return constructor.newInstance(arguments);
        } catch (ClassCastException | InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException cause) {
            throw new IllegalArgumentException();
        }
    }

    private Constructor<?> getConstructor(Class<?> serviceClass, Object[] arguments) {
        int argumentLength = arguments == null ? 0 : arguments.length;
        Constructor<?>[] constructors = serviceClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == argumentLength) {
                return constructor;
            }
        }
        throw new IllegalArgumentException();
    }

    public <T> void removeService(T service) {
        if (serviceIndex.containsKey(service)) {
            serviceRegistry.get(serviceIndex.remove(service)).remove(service);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceType) {
        if (!serviceRegistry.containsKey(serviceType)) {
            throw new IllegalStateException();
        }
        Iterator<Object> iterator = serviceRegistry.get(serviceType).iterator();
        if (!iterator.hasNext()) {
            throw new IllegalStateException();
        }
        return (T) iterator.next();
    }

    @SuppressWarnings("unchecked")
    public <T> ServiceManager<T> getServiceManager(T service) {
        if (service == null || !Proxy.isProxyClass(service.getClass())) {
            throw new IllegalArgumentException();
        }
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(service);
        if (!(invocationHandler instanceof LocalServiceProxy)) {
            throw new IllegalArgumentException();
        }
        return ((LocalServiceProxy<T>) invocationHandler).getServiceManager();
    }

}
