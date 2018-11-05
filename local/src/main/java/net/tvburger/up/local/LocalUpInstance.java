package net.tvburger.up.local;

import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.identity.Identity;
import net.tvburger.up.impl.ProtocolLifecycleManagerProvider;

import java.net.UnknownHostException;

public final class LocalUpInstance {

    private static final LocalUpInstance INSTANCE = new LocalUpInstance();
    private static final ProtocolLifecycleManagerProvider provider = new ProtocolLifecycleManagerProvider();

    public static LocalUpInstance get() {
        return INSTANCE;
    }

    private Java8Interpreter interpreter = Java8Interpreter.Factory.create();
    private UpEngine engine;
    private UpRuntime runtime = new LocalUpRuntime(Identity.ANONYMOUS);

    {
        try {
            engine = LocalUpEngine.Factory.create();
        } catch (UnknownHostException cause) {
            throw new ExceptionInInitializerError(cause);
        }
    }

    private LocalUpInstance() {
    }

    public ProtocolLifecycleManagerProvider getProvider() {
        return provider;
    }

    public Java8Interpreter getInterpreter() {
        return interpreter;
    }

    public UpEngine getEngine() {
        return engine;
    }

    public UpRuntime getRuntime() {
        return runtime;
    }

}
