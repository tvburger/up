package net.tvburger.up.local.impl;

import net.tvburger.up.Up;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.UpContext;
import net.tvburger.up.impl.UpContextImpl;
import net.tvburger.up.security.AccessDeniedException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * This proxy sets the context correct for entering and leaving the UpEngine.
 *
 * @param <T>
 */
public final class LocalClientProxy<T> implements InvocationHandler {

    public static final class Factory {

        public static Object create(LocalUpClientTarget target, UpClient client) {
            Objects.requireNonNull(client);
            return create(target, CallerInfo.Factory.create(client.getInfo()), client);
        }

        @SuppressWarnings("unchecked")
        public static Object create(LocalUpClientTarget target, CallerInfo callerInfo, Object instance) {
            Objects.requireNonNull(target);
            Objects.requireNonNull(callerInfo);
            Objects.requireNonNull(instance);
            Class<?> interfaceType = getInterfaceType(instance);
            return interfaceType == null
                    ? instance
                    : Proxy.newProxyInstance(
                    interfaceType.getClassLoader(),
                    new Class<?>[]{interfaceType},
                    new LocalClientProxy<>(target, callerInfo, instance));
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

    private final LocalUpClientTarget target;
    private final CallerInfo callerInfo;
    private final T instance;

    private LocalClientProxy(LocalUpClientTarget target, CallerInfo callerInfo, T instance) {
        this.target = target;
        this.callerInfo = callerInfo;
        this.instance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        UpContext context = Up.getContext();
        try {
            Up.setContext(createContext());
            Object object = method.invoke(instance, args);
            return object != null && !object.getClass().isPrimitive()
                    ? Factory.create(target, callerInfo, object)
                    : object;
        } catch (InvocationTargetException cause) {
            throw cause.getCause();
        } finally {
            Up.setContext(context);
        }
    }

    private UpContext createContext() throws AccessDeniedException {
        LocalUpInstance instance = target.getInstance();
        UpContextImpl context = new UpContextImpl();
        context.setCallerInfo(callerInfo);
        context.setIdentity(instance.getEngineIdentity());
        context.setEnvironment(instance.getRuntime().getEnvironment(callerInfo.getClientInfo().getEnvironmentInfo().getName()));
        context.setLocality(Locality.Factory.create(instance.getEngine()));
        return context;
    }

}
