package net.tvburger.up.behaviors;

import net.tvburger.up.security.AccessDeniedException;

public interface ManagedObject<M extends ManagedObject.Manager<I>, I extends ValueObject> {

    interface Manager<I> {

        I getInfo();

    }

    M getManager() throws AccessDeniedException;

    I getInfo();

}
