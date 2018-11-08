package net.tvburger.up.util;

import net.tvburger.up.context.UpContext;
import net.tvburger.up.spi.UpContextProvider;

public final class ThreadBasedContextProvider implements UpContextProvider {

    private static final ThreadLocal<UpContext> contexts = new InheritableThreadLocal<>();

    @Override
    public UpContext getContext() {
        return contexts.get();
    }

    @Override
    public void setContext(UpContext context) {
        contexts.set(context);
    }

}