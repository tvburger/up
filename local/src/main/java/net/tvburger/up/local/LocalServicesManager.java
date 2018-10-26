package net.tvburger.up.local;

import net.tvburger.up.ServiceInfo;
import net.tvburger.up.UpClient;
import net.tvburger.up.admin.ServiceManager;
import net.tvburger.up.impl.ServiceInfoImpl;
import net.tvburger.up.impl.ServiceManagerImpl;
import net.tvburger.up.impl.ServiceUtil;
import net.tvburger.up.logger.Logger;
import net.tvburger.up.spi.ProtocolManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;

public class LocalServicesManager {

    private final UpClient client = new UpClient() {
        @Override
        public String getEnvironment() {
            return null;
        }

        @Override
        public <T> T getService(Class<T> serviceType) {
            return LocalServicesManager.this.getService(serviceType);
        }

        @Override
        public <T> ServiceInfo<T> getServiceInfo(T service) {
            return null;
        }

        @Override
        public <T> ServiceManager<T> getServiceManager(T service) {
            return null;
        }

        @Override
        public <T, S extends T> void addTypedService(Class<T> serviceType, Class<S> serviceClass, Object... arguments) {
        }

        @Override
        public <T> void removeService(T service) {
        }

        @Override
        public <P extends ProtocolManager> boolean supportsProtocol(Class<P> protocolType) {
            return false;
        }

        @Override
        public <P extends ProtocolManager> P getProtocol(Class<P> protocolType) {
            return null;
        }
    };

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
        T service = createLoggerProxy(ServiceUtil.instantiateService(client, serviceClass, arguments), createServiceManager(serviceType));
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
