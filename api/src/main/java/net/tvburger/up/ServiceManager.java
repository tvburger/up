package net.tvburger.up;

import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedEntity;

public interface ServiceManager<T> extends Implementation, LogManager, LifecycleManager, ManagedEntity.Manager<ServiceInfo<T>> {
}
