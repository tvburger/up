package net.tvburger.up.local;

import net.tvburger.up.Up;
import net.tvburger.up.client.UpClientInfo;
import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.util.ThreadBasedContextProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

public final class LocalClientProxy<T> implements InvocationHandler {

    public static final class Factory {

        @SuppressWarnings("unchecked")
        public static Object create(UpClientInfo clientInfo, Object instance) {
            Objects.requireNonNull(clientInfo);
            Objects.requireNonNull(instance);
            Class<?> interfaceType = getInterfaceType(instance);
            return interfaceType == null
                    ? instance
                    : Proxy.newProxyInstance(
                    interfaceType.getClassLoader(),
                    new Class<?>[]{interfaceType},
                    new LocalClientProxy<>(clientInfo, instance));
        }

        private static Class<?> getInterfaceType(Object instance) {
            if (instance.getClass().isPrimitive() || instance.getClass().getName().startsWith("java.") || instance.getClass().getName().startsWith("javax.")) {
                return null;
            }
            for (Class<?> interfaze : instance.getClass().getInterfaces()) {
                return interfaze;
            }
            return null;
        }

        private Factory() {
        }

    }

    private final UpClientInfo clientInfo;
    private final T instance;

    private LocalClientProxy(UpClientInfo clientInfo, T instance) {
        this.clientInfo = clientInfo;
        this.instance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CallerInfo callerInfo = Up.getCallerInfo();
        try {
            ThreadBasedContextProvider.set(CallerInfo.Factory.create(clientInfo));
            Object object = method.invoke(instance, args);
            return object != null && !object.getClass().isPrimitive()
                    ? Factory.create(clientInfo, object)
                    : object;
        } catch (InvocationTargetException cause) {
            throw cause.getCause();
        } finally {
            ThreadBasedContextProvider.set(callerInfo);
        }
    }

}
