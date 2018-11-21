package net.tvburger.up.applications.api;

import net.tvburger.up.UpService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.UUID;

public final class ApiServiceProxy implements InvocationHandler {

    public static final class Factory {

        public static Object createProxy(UUID uuid, UpService<?> service) {
            Objects.requireNonNull(uuid);
            Objects.requireNonNull(service);
            return Proxy.newProxyInstance(service.getClass().getClassLoader(), service.getClass().getInterfaces(), new ApiServiceProxy(uuid, service));
        }

        private Factory() {
        }

    }

    private final UUID uuid;
    private final UpService<?> service;

    public ApiServiceProxy(UUID uuid, UpService<?> service) {
        this.uuid = uuid;
        this.service = service;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UpService<?> getService() {
        return service;
    }

    public Object invoke(String methodName, Object[] args) throws Throwable {
        Method method = service.getInterface().getClass().getMethod(methodName);
        return method.invoke(service);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(service.getInterface(), args);
    }

}
