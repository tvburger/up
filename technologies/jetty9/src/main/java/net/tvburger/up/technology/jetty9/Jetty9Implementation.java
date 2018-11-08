package net.tvburger.up.technology.jetty9;

import net.tvburger.up.impl.ImplementationImpl;
import net.tvburger.up.technology.servlet.JSR340Specification;

public final class Jetty9Implementation extends ImplementationImpl {

    private static final Jetty9Implementation implementation = new Jetty9Implementation();

    public static Jetty9Implementation get() {
        return implementation;
    }

    private Jetty9Implementation() {
        super(JSR340Specification.get(), "jetty", "9.4.12.v20180830");
    }

}
