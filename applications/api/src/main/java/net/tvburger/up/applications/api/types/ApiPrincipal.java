package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.security.Principal;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiPrincipal implements Principal {

    public static ApiPrincipal fromUp(Principal up) {
        if (up == null) {
            return null;
        }
        ApiPrincipal api = new ApiPrincipal();
        api.name = up.getName();
        return api;
    }

    public Principal toUp() {
        return this;
    }

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("ApiPrincipal{%s}", name);
    }

}
