package net.tvburger.up;

public interface UpClient {

    <T> T getService(Class<T> serviceType);

    <T> ServiceInfo<T> getServiceInfo(T service);

    <T> ServiceManager<T> getServiceManager(T service);

    @SuppressWarnings("unchecked")
    default <T> void addService(Class<T> serviceClass, Object... arguments) {
        if (serviceClass == null || serviceClass.getInterfaces().length == 0) {
            throw new IllegalArgumentException();
        }
        addTypedService((Class<? super T>) serviceClass.getInterfaces()[0], serviceClass, arguments);
    }

    <T, S extends T> void addTypedService(Class<T> serviceType, Class<S> serviceClass, Object... arguments);

    <T> void removeService(T service);

}
