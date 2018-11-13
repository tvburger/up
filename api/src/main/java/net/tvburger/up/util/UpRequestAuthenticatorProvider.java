package net.tvburger.up.util;

import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.spi.UpRequestAuthenticator;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

public final class UpRequestAuthenticatorProvider {

    public static final class Factory {

        public static UpRequestAuthenticatorProvider create() {
            Set<UpRequestAuthenticator> handlers = new HashSet<>();
            for (UpRequestAuthenticator handler : ServiceLoader.load(UpRequestAuthenticator.class)) {
                handlers.add(handler);
            }
            return new UpRequestAuthenticatorProvider(handlers);
        }

        private Factory() {
        }

    }

    private final Set<UpRequestAuthenticator> handlers;

    private UpRequestAuthenticatorProvider(Set<UpRequestAuthenticator> handlers) {
        this.handlers = handlers;
    }

    public UpRequestAuthenticator get(Specification technology, Specification authenticationProtocol) {
        for (UpRequestAuthenticator handler : handlers) {
            if (handler.supportsTechnology(technology) && handler.getSpecification().equals(authenticationProtocol)) {
                return handler;
            }
        }
        return null;
    }

}
