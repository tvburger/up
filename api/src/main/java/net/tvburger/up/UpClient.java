package net.tvburger.up;

public interface UpClient {

    <T> T getService(Class<T> serviceType);

    <T> void addService(Class<T> serviceClass, Object... arguments);

    <T> void removeService(T service);

}
