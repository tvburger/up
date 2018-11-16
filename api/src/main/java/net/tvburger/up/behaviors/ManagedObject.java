package net.tvburger.up.behaviors;

import net.tvburger.up.security.AccessDeniedException;

/**
 * This class represents an object that is described by the Info object, and its state can be altered by the Manager.
 * The analogy is the "execute" -> object, "read" -> Info, and "write" -> Manager permission.
 *
 * @param <M>
 * @param <I>
 */
public interface ManagedObject<M extends ManagedObject.Manager<I>, I extends ManagedObject.Info> {

    interface Manager<I> {

        I getInfo();

    }

    interface Info extends ValueObject {
    }

    M getManager() throws AccessDeniedException;

    I getInfo();

}
