package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedEntity;

public interface Service<T> extends ManagedEntity<ServiceManager<T>, ServiceInfo<T>> {

    T getInterface();

}
