package net.tvburger.up;

import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.LogManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.behaviors.ManagedObject;

import java.net.URI;

public interface UpEndpoint<M extends UpEndpoint.Manager<I>, I extends UpEndpoint.Info> extends ManagedEntity<M, I> {

    interface Info extends ManagedEntity.Info {

        URI getEndpointUri();

        UpEndpointTechnologyInfo getEndpointTechnologyInfo();

        UpApplication.Info getApplicationInfo();

    }

    interface Manager<I extends Info> extends LogManager, LifecycleManager, ManagedObject.Manager<I> {
    }

}
