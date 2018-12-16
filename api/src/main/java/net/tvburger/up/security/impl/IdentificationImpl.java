package net.tvburger.up.security.impl;

import net.tvburger.up.security.Identification;
import net.tvburger.up.security.Identity;

import java.security.Principal;
import java.security.PublicKey;
import java.util.Objects;
import java.util.UUID;

public class IdentificationImpl implements Identification {

    public static final class Factory {

        public static IdentificationImpl create(final Identity identity) {
            Objects.requireNonNull(identity);
            return new IdentificationImpl(identity.getPrincipal(), identity.getPublicKey(), identity.getUuid());
        }

        public static IdentificationImpl create(final Principal principal, final PublicKey publicKey, final UUID uuid) {
            Objects.requireNonNull(principal);
            Objects.requireNonNull(publicKey);
            Objects.requireNonNull(uuid);
            return new IdentificationImpl(principal, publicKey, uuid);
        }

    }

    private final Principal principal;
    private final PublicKey publicKey;
    private final UUID uuid;

    protected IdentificationImpl(Principal principal, PublicKey publicKey, UUID uuid) {
        this.principal = principal;
        this.publicKey = publicKey;
        this.uuid = uuid;
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
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return String.format("Identification{%s, %s}", principal, uuid);
    }

    @Override
    public boolean equals(final Object object) {
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
