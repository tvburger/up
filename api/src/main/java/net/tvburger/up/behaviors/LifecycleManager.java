package net.tvburger.up.behaviors;

/**
 * Used to mark a Type that it manages a specific lifecycle. The lifecycle consist of init, start, stop and destroy.
 */
public interface LifecycleManager {

    void init() throws LifecycleException;

    void start() throws LifecycleException;

    void stop() throws LifecycleException;

    void destroy() throws LifecycleException;

}
