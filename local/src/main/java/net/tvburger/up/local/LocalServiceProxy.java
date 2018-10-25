package net.tvburger.up.local;

import net.tvburger.up.ServiceManager;
import net.tvburger.up.logger.LogLevel;
import net.tvburger.up.logger.LogStatement;
import net.tvburger.up.logger.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class LocalServiceProxy<T> implements InvocationHandler {

    private final T service;
    private final ServiceManager<T> serviceManager;
    private final Logger logger;

    public LocalServiceProxy(T service, ServiceManager<T> serviceManager, Logger logger) {
        this.service = service;
        this.serviceManager = serviceManager;
        this.logger = logger;
    }

    public T getService() {
        return service;
    }

    public ServiceManager<T> getServiceManager() {
        return serviceManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean logMethod = serviceManager.isLogged() && !method.getDeclaringClass().equals(Object.class);
        return logMethod ? invokeLogged(method, args) : method.invoke(service, args);
    }

    public Object invokeLogged(Method method, Object[] args) throws Throwable {
        LogStatement.Builder builder = new LogStatement.Builder()
                .withServiceInfo(getServiceManager().getServiceInfo())
                .withLogLevel(LogLevel.TRACE);
        logger.log(builder.withMessage(buildMessage(method, args)).build());
        try {
            Object result = method.invoke(service, args);
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
