package net.tvburger.up.runtime.util;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.runtime.spi.UpRequestAuthenticator;

public final class UpRequestAuthenticators {

    private static final UpRequestAuthenticatorProvider provider = UpRequestAuthenticatorProvider.Factory.create();

    public static UpRequestAuthenticator get(Specification technology, Specification authenticationProtocol) {
        return provider.get(technology, authenticationProtocol);
    }

    private UpRequestAuthenticators() {
    }

}
