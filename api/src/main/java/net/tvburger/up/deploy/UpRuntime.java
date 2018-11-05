package net.tvburger.up.deploy;

import net.tvburger.up.Environment;
import net.tvburger.up.identity.Entity;

import java.util.Set;

public interface UpRuntime extends Entity {

    Set<UpEngine> getEngines();

    Set<Environment> getEnvironments();

    Environment getEnvironment(String environmentName);

}
