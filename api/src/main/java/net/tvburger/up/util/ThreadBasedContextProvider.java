package net.tvburger.up.util;

import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.UpServiceContext;
import net.tvburger.up.spi.UpContextProvider;

public final class ThreadBasedContextProvider implements UpContextProvider {

    private static final ThreadLocal<UpServiceContext> serviceContexts = new ThreadLocal<>();
    private static final ThreadLocal<CallerInfo> callerInfos = new ThreadLocal<>();

    public static void set(UpServiceContext context) {
        serviceContexts.set(context);
    }

    public static void set(CallerInfo callerInfo) {
        callerInfos.set(callerInfo);
    }

    @Override
    public UpServiceContext getServiceContext() {
        return serviceContexts.get();
    }

    @Override
    public CallerInfo getCallerInfo() {
        return callerInfos.get();
    }

}