package net.tvburger.up.local;

import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.impl.ImplementationImpl;
import net.tvburger.up.impl.SpecificationImpl;

public final class LocalJavaImplementation extends ImplementationImpl {

    private static final Implementation singleton = new LocalJavaImplementation(
            SpecificationImpl.Factory.create(
                    Runtime.class.getPackage().getImplementationTitle(),
                    Runtime.class.getPackage().getSpecificationVersion()),
            Runtime.class.getPackage().getSpecificationTitle(),
            Runtime.class.getPackage().getImplementationVersion());

    public static Implementation get() {
        return singleton;
    }

    private LocalJavaImplementation(Specification specification, String name, String version) {
        super(specification, name, version);
    }

}
