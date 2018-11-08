package net.tvburger.up.behaviors;

import net.tvburger.up.security.Identification;

public interface ManagedEntity<M extends ManagedObject.Manager<I>, I extends ManagedEntity.Info> extends Entity, ManagedObject<M, I> {

    interface Info extends ManagedObject.Info {

        Identification getIdentification();

    }

}
