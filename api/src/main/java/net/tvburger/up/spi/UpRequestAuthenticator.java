package net.tvburger.up.spi;

import net.tvburger.up.behaviors.Specification;

public interface UpRequestAuthenticator {

    Specification getSpecification();

    boolean supportsTechnology(Specification technology);

}
