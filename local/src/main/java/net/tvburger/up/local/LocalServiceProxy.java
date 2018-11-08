package net.tvburger.up.local;

import net.tvburger.up.Environment;
import net.tvburger.up.Service;
import net.tvburger.up.Up;
import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.UpServiceContext;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.impl.UpServiceContextImpl;
import net.tvburger.up.logger.LogLevel;
import net.tvburger.up.logger.LogStatement;
import net.tvburger.up.logger.UpLogger;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.ThreadBasedContextProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public final class LocalServiceProxy<T> implements InvocationHandler {

    private final UpEngine engine;
    private final Service<T> service;
    private final Identity serviceIdentity;
    private final Environment environment;
    private final UpLogger logger;

    public LocalServiceProxy(UpEngine engine, Service<T> service, Identity serviceIdentity, Environment environment, UpLogger logger) {
        this.engine = engine;
        this.service = service;
        this.serviceIdentity = serviceIdentity;
        this.environment = environment;
        this.logger = logger;
    }

    public Service<T> getService() {
        return service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        UpServiceContext serviceContext = Up.getServiceContext();
        CallerInfo callerInfo = Up.getCallerInfo();
        try {
            CallerInfo thisCaller;
            if (serviceContext != null) {
                thisCaller = CallerInfo.Factory.create(serviceContext.getInfo());
                ThreadBasedContextProvider.set(thisCaller);
            } else {
                thisCaller = callerInfo;
            }
            UpServiceContext thisServiceContext = createServiceContext();
            ThreadBasedContextProvider.set(thisServiceContext);
            boolean logMethod = service.getManager().isLogged() && !method.getDeclaringClass().equals(Object.class);
            return logMethod ? invokeLogged(method, args, thisCaller, thisServiceContext) : method.invoke(service.getInterface(), args);
        } catch (InvocationTargetException cause) {
            throw cause.getCause();
        } finally {
            if (serviceContext != null) {
                ThreadBasedContextProvider.set(callerInfo);
            }
            ThreadBasedContextProvider.set(serviceContext);
        }
    }

    private UpServiceContext createServiceContext() {
        UpServiceContextImpl context = new UpServiceContextImpl();
        context.setInfo(service.getInfo());
        context.setIdentity(serviceIdentity);
        context.setEnvironment(environment);
        context.setLocality(Locality.Factory.create(engine));
        context.setEngine(engine);
        return context;
    }

    public Object invokeLogged(Method method, Object[] args, CallerInfo callerInfo, UpServiceContext serviceContext) throws Throwable {
        LogStatement.Builder builder = new LogStatement.Builder().withLogLevel(LogLevel.TRACE);
        String caller = callerInfo.getServiceInfo() == null ? callerInfo.getClientInfo().toString() : callerInfo.getServiceInfo().toString();
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
