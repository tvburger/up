package net.tvburger.up.util;

import net.tvburger.up.behaviors.impl.SpecificationImpl;

public final class Java8Specification extends SpecificationImpl {

    private static final Java8Specification instance = new Java8Specification();

    public static Java8Specification get() {
        return instance;
    }

    private Java8Specification() {
        super("Java Platform API Specification", "1.8");
    }

}
