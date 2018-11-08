package net.tvburger.up.technology.jersey2;

import net.tvburger.up.impl.ImplementationImpl;
import net.tvburger.up.technology.jsr370.Jsr370Specification;

public final class Jersey2Implementation extends ImplementationImpl {

    private static final Jersey2Implementation implementation = new Jersey2Implementation();

    public static Jersey2Implementation get() {
        return implementation;
    }

    private Jersey2Implementation() {
        super(Jsr370Specification.get(), "jersey", "2.27.0");
    }

}
