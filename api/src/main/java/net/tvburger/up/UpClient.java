package net.tvburger.up;

public interface UpClient {

    <T> T getService(Class<T> serviceType);

    <T> ServiceInfo<T> getServiceInfo(T service);

    <T> ServiceManager<T> getServiceManager(T service);

    <T> void addService(Class<T> serviceClass, Object... arguments);

    <T> void removeService(T service);

}
