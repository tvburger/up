package net.tvburger.up.util;

import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.UpContext;
import net.tvburger.up.spi.UpContextProvider;

public final class ThreadBasedContextProvider implements UpContextProvider {

    private static final ThreadLocal<UpContext> contexts = new InheritableThreadLocal<>();
    private static final ThreadLocal<CallerInfo> callerInfos = new InheritableThreadLocal<>();

    public static void set(UpContext context) {
        contexts.set(context);
    }

    @Override
    public UpContext getContext() {
        return contexts.get();
    }

}