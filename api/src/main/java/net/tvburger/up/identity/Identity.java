package net.tvburger.up.identity;

import java.security.Principal;
import java.security.PublicKey;

public interface Identity {

    Identity ANONYMOUS = new Identity() {
        @Override
        public Principal getPrincipal() {
            return () -> "Anonymous";
        }

        @Override
        public PublicKey getPublicKey() {
            return null;
        }
    };

    Principal getPrincipal();

    PublicKey getPublicKey();

}
