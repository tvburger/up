package net.tvburger.up;

import net.tvburger.up.behaviors.*;

import java.util.UUID;

public interface UpService<T> extends ManagedEntity<UpService.Manager<T>, UpService.Info<T>> {

    interface Info<T> extends Specification, ManagedEntity.Info {

        Class<T> getServiceType();

        UUID getServiceInstanceId();

        UpApplication.Info getApplicationInfo();

    }

    interface Manager<T> extends Implementation, LogManager, LifecycleManager, ManagedEntity.Manager<Info<T>> {
    }

    T getInterface();

}
