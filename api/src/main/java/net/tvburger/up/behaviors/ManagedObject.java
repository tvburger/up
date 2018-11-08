package net.tvburger.up.behaviors;

import net.tvburger.up.security.AccessDeniedException;

public interface ManagedObject<M extends ManagedObject.Manager<I>, I extends ManagedObject.Info> {

    interface Manager<I> {

        I getInfo();

    }

    interface Info extends ValueObject {
    }

    M getManager() throws AccessDeniedException;

    I getInfo();

}
