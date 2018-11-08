package net.tvburger.up.security;

import java.security.PrivateKey;

public interface Identity extends Identification {

    PrivateKey getPrivateKey();

}
