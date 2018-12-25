package net.tvburger.up.runtime.context;

import net.tvburger.up.UpRuntimeInfo;
import net.tvburger.up.runtime.UpEngine;

import java.io.Serializable;
import java.util.Objects;

public final class Locality implements Serializable {

    public static final class Factory {

        public static Locality create(UpEngine engine) {
            Objects.requireNonNull(engine);
            return create(engine.getRuntime().getInfo(), engine.getInfo());
        }

        public static Locality create(UpRuntimeInfo runtimeInfo, UpEngine.Info engineInfo) {
            Objects.requireNonNull(runtimeInfo);
            Objects.requireNonNull(engineInfo);
            return create(runtimeInfo, engineInfo, Thread.currentThread().getName());
        }

        public static Locality create(UpRuntimeInfo runtimeInfo, UpEngine.Info engineInfo, String threadName) {
            Objects.requireNonNull(runtimeInfo);
            Objects.requireNonNull(engineInfo);
            Objects.requireNonNull(threadName);
            return new Locality(runtimeInfo, engineInfo, threadName);
        }

        private Factory() {
        }

    }

    private final UpRuntimeInfo runtimeInfo;
    private final UpEngine.Info engineInfo;
    private final String threadName;

    private Locality(UpRuntimeInfo runtimeInfo, UpEngine.Info engineInfo, String threadName) {
        this.runtimeInfo = runtimeInfo;
        this.engineInfo = engineInfo;
        this.threadName = threadName;
    }

    public UpRuntimeInfo getRuntimeInfo() {
        return runtimeInfo;
    }

    public UpEngine.Info getEngineInfo() {
        return engineInfo;
    }

    public String getThreadName() {
        return threadName;
    }

    @Override
    public String toString() {
        return String.format("Locality{%s, %s, %s}", runtimeInfo, engineInfo, threadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runtimeInfo, engineInfo, threadName);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Locality)) {
            return false;
        }
        Locality other = (Locality) object;
        return Objects.equals(runtimeInfo, other.runtimeInfo)
                && Objects.equals(engineInfo, other.engineInfo)
                && Objects.equals(threadName, other.threadName);
    }

}
