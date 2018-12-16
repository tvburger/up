package net.tvburger.up.util;

import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;
import net.tvburger.up.security.impl.IdentificationImpl;

import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

public final class Identities {

    public static Identity createAnonymous() {
        return new Identity() {

            private final UUID uuid = UUID.randomUUID();

            @Override
            public PrivateKey getPrivateKey() {
                return null;
            }

            @Override
            public Principal getPrincipal() {
                return () -> "Anonymous";
            }

            @Override
            public PublicKey getPublicKey() {
                return null;
            }

            @Override
            public UUID getUuid() {
                return uuid;
            }

            @Override
            public String toString() {
                return "Anonymous";
            }

        };
    }

    public static Identification getSafeIdentification(final Identity identity) {
        return IdentificationImpl.Factory.create(identity);
    }

    private Identities() {
    }

}
