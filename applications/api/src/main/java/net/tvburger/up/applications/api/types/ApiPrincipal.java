package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.security.Principal;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiPrincipal implements Principal {

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
