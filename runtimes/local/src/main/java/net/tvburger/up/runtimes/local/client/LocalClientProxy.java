package net.tvburger.up.runtimes.local.client;

import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.runtime.context.CallerInfo;
import net.tvburger.up.runtime.context.Locality;
import net.tvburger.up.runtime.context.TransactionInfo;
import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.runtime.impl.UpContextHolder;
import net.tvburger.up.runtime.impl.UpContextImpl;
import net.tvburger.up.runtimes.local.LocalUpInstance;
import net.tvburger.up.security.AccessDeniedException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.Objects;

/**
 * This proxy sets the context correct for entering and leaving the UpEngine.
 *
 * @param <T>
 */
public final class LocalClientProxy<T> implements InvocationHandler {

    public static final class Factory {

        public static UpClient create(LocalUpClientTarget target, UpClient client) {
            Objects.requireNonNull(target);
            Objects.requireNonNull(client);
            return create(target, client.getInfo(), client);
        }

        @SuppressWarnings("unchecked")
        public static <T> T create(LocalUpClientTarget target, UpClient.Info clientInfo, T instance) {
            Objects.requireNonNull(target);
            Objects.requireNonNull(clientInfo);
            Objects.requireNonNull(instance);
            Class<?> interfaceType = getInterfaceType(instance);
            return interfaceType == null
                    ? instance
                    : (T) Proxy.newProxyInstance(
                    interfaceType.getClassLoader(),
                    new Class<?>[]{interfaceType},
                    new LocalClientProxy<>(target, clientInfo, instance));
        }

        private static Class<?> getInterfaceType(Object instance) {
            if (instance.getClass().isPrimitive()
                    || instance.getClass().getName().startsWith("java.")
                    || instance.getClass().getName().startsWith("javax.")
                    || instance instanceof UpClientTarget) {
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
    private final UpClient.Info clientInfo;
    private final T instance;

    private LocalClientProxy(LocalUpClientTarget target, UpClient.Info clientInfo, T instance) {
        this.target = target;
        this.clientInfo = clientInfo;
        this.instance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        UpContext context = UpContextHolder.getContext();
        try {
            UpContextHolder.setContext(createContext(method));
            Object object = method.invoke(instance, args);
            return object != null && !object.getClass().isPrimitive()
                    ? Factory.create(target, clientInfo, object)
                    : object;
        } catch (InvocationTargetException cause) {
            throw cause.getCause();
        } finally {
            UpContextHolder.setContext(context);
        }
    }

    private UpContext createContext(Method method) throws AccessDeniedException {
        LocalUpInstance localUpInstance = target.getInstance();
        UpContextImpl context = new UpContextImpl();
        context.setTransactionInfo(createTransactionInfo(method));
        context.setCallerInfo(CallerInfo.Factory.create(context));
        context.setEnvironment(localUpInstance.getRuntime().getEnvironment(clientInfo.getEnvironmentInfo().getName()));
        context.setIdentity(localUpInstance.getEngineIdentity());
        context.setEngine(localUpInstance.getEngine());
        context.setRuntime(localUpInstance.getRuntime());
        context.setLocality(Locality.Factory.create(localUpInstance.getEngine()));
        return context;
    }

    private TransactionInfo createTransactionInfo(Method method) {
        return TransactionInfo.Factory.create(
                clientInfo.getIdentification().getPrincipal(),
                URI.create("java://" + getClassName() + "/" + method.getName()));
    }

    private String getClassName() {
        return Proxy.isProxyClass(instance.getClass())
                ? instance.getClass().getInterfaces()[0].getSimpleName()
                : instance.getClass().getSimpleName();
    }

}
