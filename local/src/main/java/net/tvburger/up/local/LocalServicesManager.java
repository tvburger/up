package net.tvburger.up.local;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class LocalServicesManager {

    private final Map<Class<?>, Set<Object>> serviceRegistry = new HashMap<>();
    private final Map<Object, Class<?>> serviceIndex = new HashMap<>();

    public <T> void addService(Class<T> serviceClass, Object[] arguments) {
        Class<?> interfaze = getInterface(serviceClass);
        T service = instantiateService(serviceClass, arguments);
        Set<Object> services = serviceRegistry.computeIfAbsent(interfaze, (key) -> new HashSet<>());
        services.add(service);
        serviceIndex.put(service, interfaze);
    }

    private Class<?> getInterface(Class<?> serviceClass) {
        Class<?>[] interfaces = serviceClass.getInterfaces();
        if (interfaces.length == 0) {
            throw new IllegalArgumentException("Must implement at least 1 interface!");
        }
        return interfaces[0];
    }

    @SuppressWarnings("unchecked")
    private <T> T instantiateService(Class<T> serviceClass, Object[] arguments) {
        try {
            Constructor<T> constructor = (Constructor<T>) getConstructor(serviceClass, arguments);
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

}
