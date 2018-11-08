package net.tvburger.up;

import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedObject;

public interface EndpointManager<I extends Endpoint.Info> extends LogManager, LifecycleManager, ManagedObject.Manager<I> {
}
