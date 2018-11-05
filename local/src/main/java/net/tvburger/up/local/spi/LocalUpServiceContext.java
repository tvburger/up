package net.tvburger.up.local.spi;

import net.tvburger.up.ServiceInfo;
import net.tvburger.up.deploy.UpLanguageInterpreter;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.impl.ServiceInfoImpl;
import net.tvburger.up.local.LocalUpInstance;
import net.tvburger.up.service.InfraTraceElement;
import net.tvburger.up.service.UpServiceContext;

public class LocalUpServiceContext implements UpServiceContext {

    private final static ThreadLocal<LocalUpServiceContext> upServiceContext = ThreadLocal.withInitial(() ->
            new LocalUpServiceContext(LocalUpInstance.get().getInterpreter(),
                    new ServiceInfoImpl<>(
                            UpRuntime.class,
                            LocalUpInstance.get().getRuntime().getIdentity(),
                            LocalUpInstance.get().getEngine().getUUID(),
                            null)));

    public static LocalUpServiceContext get() {
        return upServiceContext.get();
    }

    public static void set(LocalUpServiceContext context) {
        upServiceContext.set(context);
    }

    private final UpLanguageInterpreter interpreter;
    private final ServiceInfo serviceInfo;

    public LocalUpServiceContext(UpLanguageInterpreter interpreter, ServiceInfo serviceInfo) {
        this.interpreter = interpreter;
        this.serviceInfo = serviceInfo;
    }

    @Override
    public UpLanguageInterpreter getLanguageInterpreter() {
        return interpreter;
    }

    @Override
    public InfraTraceElement getLocation() {
        return new InfraTraceElement(LocalUpInstance.get().getEngine().getHost(), Thread.currentThread().getName());
    }

    @Override
    public ServiceInfo<?> getServiceInfo() {
        return serviceInfo;
    }
}
