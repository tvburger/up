package net.tvburger.up.util;

import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;

import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;

public final class Identities {

    public static final Identity ANONYMOUS = new Identity() {
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
        public String toString() {
            return "Anonymous";
        }

    };

    public static Identification getSafeIdentification(Identity identity) {
        return new Identification() {
            @Override
            public Principal getPrincipal() {
                return identity.getPrincipal();
            }

            @Override
            public PublicKey getPublicKey() {
                return identity.getPublicKey();
            }

            @Override
            public String toString() {
                return String.format("Identification(%s, %s)", getPrincipal().getName(), getPublicKey());
            }
        };
    }

    private Identities() {
    }

}
