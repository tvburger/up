package net.tvburger.up.behaviors;

/**
 * Used to mark a Type that implements a specific Specification.
 */
public interface Implementation {

    String getImplementationName();

    String getImplementationVersion();

    Specification getSpecification();

}
