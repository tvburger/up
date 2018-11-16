package net.tvburger.up.runtime.impl;

import net.tvburger.up.runtime.context.UpContext;

public final class UpContextHolder {

    private static final ThreadLocal<UpContext> contexts = new InheritableThreadLocal<>();

    public static UpContext getContext() {
        return contexts.get();
    }

    public static void setContext(UpContext context) {
        contexts.set(context);
    }

    private UpContextHolder() {
    }

}
