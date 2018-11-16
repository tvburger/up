package net.tvburger.up.security.impl;

import net.tvburger.up.security.Identification;

import java.security.Principal;
import java.security.PublicKey;
import java.util.Objects;

public class IdentificationImpl implements Identification {

    public static final class Factory {

        public static IdentificationImpl create(Principal principal, PublicKey publicKey) {
            Objects.requireNonNull(principal);
            Objects.requireNonNull(publicKey);
            return new IdentificationImpl(principal, publicKey);
        }

    }

    private final Principal principal;
    private final PublicKey publicKey;

    protected IdentificationImpl(Principal principal, PublicKey publicKey) {
        this.principal = principal;
        this.publicKey = publicKey;
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof Identification
                        && Objects.equals(principal, ((Identification) object).getPrincipal())
                        && Objects.equals(publicKey, ((Identification) object).getPublicKey()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(principal) * 31 + Objects.hashCode(publicKey) * 47 + 19;
    }

}
