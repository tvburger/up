package net.tvburger.up.runtimes.local;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.runtime.impl.UpContextImpl;
import net.tvburger.up.runtime.impl.UpServiceImpl;
import net.tvburger.up.runtime.util.UpContextHolder;
import net.tvburger.up.runtimes.local.client.UpProxyException;
import net.tvburger.up.security.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * This proxy sets the context correct for invocations to a UpService.
 *
 * @param <T>
 */
public final class LocalServiceProxy<T> implements InvocationHandler {

    public static final class Factory {

        @SuppressWarnings("unchecked")
        public static <T> T create(T service, UpApplication application, Identity serviceIdentity, UpService.Manager<T> serviceManager) {
            Objects.requireNonNull(service);
            Objects.requireNonNull(application);
            Objects.requireNonNull(serviceIdentity);
            Objects.requireNonNull(serviceManager);
            return (T) Proxy.newProxyInstance(
                    service.getClass().getClassLoader(),
                    service.getClass().getInterfaces(),
                    new LocalServiceProxy<>(new UpServiceImpl<>(application, serviceManager, service), serviceIdentity));
        }

        private Factory() {
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(LocalServiceProxy.class);

    private final UpService<T> service;
    private final Identity serviceIdentity;

    private LocalServiceProxy(UpService<T> service, Identity serviceIdentity) {
        this.service = service;
        this.serviceIdentity = serviceIdentity;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        UpContext callerContext = UpContextHolder.getContext();
        try {
            if (service.getManager().getState() != LifecycleManager.State.ACTIVE) {
                throw new UpProxyException("We are not active: " + service.getInfo());
            }
            UpContextHolder.setContext(UpContextImpl.Factory.createServiceContext(service, serviceIdentity, callerContext));
            boolean logMethod = service.getManager().isLogged() && !method.getDeclaringClass().equals(Object.class);
            return logMethod ? invokeLogged(method, args) : method.invoke(service.getInterface(), args);
        } catch (InvocationTargetException cause) {
            throw cause.getCause();
        } finally {
            UpContextHolder.setContext(callerContext);
        }
    }

    public Object invokeLogged(Method method, Object[] args) throws Throwable {
        logger.info(buildMessage(method, args));
        try {
            Object result = method.invoke(service.getInterface(), args);
            logger.info("Returning " + Objects.toString(result));
            return result;
        } catch (Throwable t) {
            logger.warn("Throwing " + t.toString());
            throw t;
        }
    }

    private String buildMessage(Method method, Object[] args) {
        StringBuilder sb = new StringBuilder("Invocation of ");
        sb.append(method.getName()).append('(');
        if (args != null) {
            for (Object arg : args) {
                sb.append(Objects.toString(arg)).append(", ");
            }
            if (args.length > 0) {
                sb.setLength(sb.length() - 2);
            }
        }
        sb.append(')');
        return sb.toString();
    }

}
