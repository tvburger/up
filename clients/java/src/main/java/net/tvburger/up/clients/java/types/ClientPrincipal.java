package net.tvburger.up.clients.java.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.security.Principal;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ClientPrincipal implements Principal {

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("ClientPrincipal{%s}", name);
    }

}
