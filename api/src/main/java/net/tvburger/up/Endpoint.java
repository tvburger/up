package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedObject;

public interface Endpoint<M extends EndpointManager<I>, I extends EndpointInfo> extends ManagedObject<M, I> {
}
