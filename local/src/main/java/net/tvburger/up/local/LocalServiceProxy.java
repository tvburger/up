package net.tvburger.up.local;

import net.tvburger.up.Service;
import net.tvburger.up.local.spi.LocalUpServiceContext;
import net.tvburger.up.logger.LogLevel;
import net.tvburger.up.logger.LogStatement;
import net.tvburger.up.logger.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class LocalServiceProxy<T> implements InvocationHandler {

    private final Service<T> service;
    private final Logger logger;

    public LocalServiceProxy(Service<T> service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    public Service<T> getService() {
        return service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LocalUpServiceContext context = LocalUpServiceContext.get();
        LocalUpServiceContext.set(new LocalUpServiceContext(LocalUpInstance.get().getInterpreter(), service.getInfo()));
        try {
            boolean logMethod = service.getManager().isLogged() && !method.getDeclaringClass().equals(Object.class);
            return logMethod ? invokeLogged(method, args) : method.invoke(service.getService(), args);
        } finally {
            LocalUpServiceContext.set(context);
        }
    }

    public Object invokeLogged(Method method, Object[] args) throws Throwable {
        LogStatement.Builder builder = new LogStatement.Builder().withLogLevel(LogLevel.TRACE);
        logger.log(builder.withMessage(buildMessage(method, args)).build());
        try {
            Object result = method.invoke(service.getService(), args);
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
