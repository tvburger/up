package net.tvburger.up.technology.servlet;

import net.tvburger.up.impl.SpecificationImpl;

public final class JSR340Specification extends SpecificationImpl {

    private static final JSR340Specification specification = new JSR340Specification();

    public static JSR340Specification get() {
        return specification;
    }

    private JSR340Specification() {
        super("servlet", "3.1");
    }

}
