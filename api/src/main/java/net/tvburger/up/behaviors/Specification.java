package net.tvburger.up.behaviors;

/**
 * Used to mark a Type that conforms to a specific Specification.
 */
public interface Specification extends ValueObject {

    String getSpecificationName();

    String getSpecificationVersion();

}
