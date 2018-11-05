package net.tvburger.up;

import net.tvburger.up.identity.Identity;

import java.io.Serializable;

public interface EnvironmentInfo extends Serializable {

    String getName();

    Identity getEnvironmentIdentity();

}
