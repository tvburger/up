package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.security.Identification;

import java.security.Principal;
import java.security.PublicKey;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiIdentification implements Identification {

    public static ApiIdentification fromUp(Identification up) {
        ApiIdentification api = new ApiIdentification();
        api.principal = ApiPrincipal.fromUp(up.getPrincipal());
        api.publicKey = ApiPublicKey.fromUp(up.getPublicKey());
        return api;
    }

    private ApiPrincipal principal;
    private ApiPublicKey publicKey;

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return String.format("ApiIdentification{%s, %s}", principal, publicKey);
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
        return 3 + Objects.hashCode(principal) * 31 + Objects.hashCode(publicKey) * 47 + 19;
    }

}
