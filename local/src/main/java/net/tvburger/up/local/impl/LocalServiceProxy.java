package net.tvburger.up.local.impl;

import net.tvburger.up.Service;
import net.tvburger.up.Up;
import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.UpContext;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.impl.UpContextImpl;
import net.tvburger.up.logger.LogLevel;
import net.tvburger.up.logger.LogStatement;
import net.tvburger.up.logger.UpLogger;
import net.tvburger.up.security.Identity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * This proxy sets the context correct for invocations to a Service.
 *
 * @param <T>
 */
public final class LocalServiceProxy<T> implements InvocationHandler {

    private final UpEngine engine;
    private final Service<T> service;
    private final Identity serviceIdentity;
    private final UpLogger logger;

    public LocalServiceProxy(UpEngine engine, Service<T> service, Identity serviceIdentity, UpLogger logger) {
        this.engine = engine;
        this.service = service;
        this.serviceIdentity = serviceIdentity;
        this.logger = logger;
    }

    public Service<T> getService() {
        return service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        UpContext callerContext = Up.getContext();
        try {
            UpContext serviceContext = createContext(callerContext);
            Up.setContext(serviceContext);
            boolean logMethod = service.getManager().isLogged() && !method.getDeclaringClass().equals(Object.class);
            return logMethod ? invokeLogged(method, args, serviceContext) : method.invoke(service.getInterface(), args);
        } catch (InvocationTargetException cause) {
            throw cause.getCause();
        } finally {
            Up.setContext(callerContext);
        }
    }

    private UpContext createContext(UpContext callerContext) {
        UpContextImpl context = new UpContextImpl();
        context.setCallerInfo(CallerInfo.Factory.create(callerContext));
        context.setServiceInfo(service.getInfo());
        context.setIdentity(serviceIdentity);
        context.setEnvironment(callerContext.getEnvironment());
        context.setLocality(Locality.Factory.create(engine));
        return context;
    }

    public Object invokeLogged(Method method, Object[] args, UpContext serviceContext) throws Throwable {
        LogStatement.Builder builder = new LogStatement.Builder().withLogLevel(LogLevel.TRACE);
        CallerInfo callerInfo = serviceContext.getCallerInfo();
        String caller = callerInfo.getServiceInfo() == null ? Objects.toString(callerInfo.getClientInfo()) : Objects.toString(callerInfo.getServiceInfo());
        logger.log(builder.withMessage(buildMessage(method, args)).build());
        try {
            logger.log(builder.withMessage("Called by " + caller).build());
            logger.log(builder.withMessage("Context is " + serviceContext).build());
            Object result = method.invoke(service.getInterface(), args);
            logger.log(builder.withMessage("Returning " + Objects.toString(result)).build());
            return result;
        } catch (Throwable t) {
            logger.log(builder.withMessage("Throwing " + t.toString()).build());
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
