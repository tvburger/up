package net.tvburger.up.context;

import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.deploy.UpEngineInfo;
import net.tvburger.up.deploy.UpRuntimeInfo;

import java.io.Serializable;
import java.util.Objects;

public final class Locality implements Serializable {

    public static final class Factory {

        public static Locality create(UpEngine engine) {
            Objects.requireNonNull(engine);
            return create(engine.getRuntime().getInfo(), engine.getInfo());
        }

        public static Locality create(UpRuntimeInfo runtimeInfo, UpEngineInfo engineInfo) {
            return create(runtimeInfo, engineInfo, Thread.currentThread().getName());
        }

        public static Locality create(UpRuntimeInfo runtimeInfo, UpEngineInfo engineInfo, String threadName) {
            Objects.requireNonNull(runtimeInfo);
            Objects.requireNonNull(engineInfo);
            Objects.requireNonNull(threadName);
            return new Locality(runtimeInfo, engineInfo, threadName);
        }

        private Factory() {
        }

    }

    private final UpRuntimeInfo runtimeInfo;
    private final UpEngineInfo engineInfo;
    private final String threadName;

    private Locality(UpRuntimeInfo runtimeInfo, UpEngineInfo engineInfo, String threadName) {
        this.runtimeInfo = runtimeInfo;
        this.engineInfo = engineInfo;
        this.threadName = threadName;
    }

    public UpRuntimeInfo getRuntimeInfo() {
        return runtimeInfo;
    }

    public UpEngineInfo getEngineInfo() {
        return engineInfo;
    }

    public String getThreadName() {
        return threadName;
    }

}
