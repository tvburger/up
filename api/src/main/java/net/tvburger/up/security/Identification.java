package net.tvburger.up.security;

import net.tvburger.up.behaviors.ValueObject;

import java.security.Principal;
import java.security.PublicKey;

public interface Identification extends ValueObject {

    Principal getPrincipal();

    PublicKey getPublicKey();

}
