package net.tvburger.up.behaviors;

import net.tvburger.up.security.Identification;

/**
 * This interface is used to mark types that can be identified by an Identification.
 */
public interface Entity {

    Identification getIdentification();

}
