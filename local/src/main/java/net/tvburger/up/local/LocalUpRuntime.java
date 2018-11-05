package net.tvburger.up.local;

import net.tvburger.up.Environment;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.deploy.UpRuntime;
import net.tvburger.up.identity.Identity;

import java.util.*;

public class LocalUpRuntime implements UpRuntime {

    private final Map<String, Environment> environments = new HashMap<>();
    private final Identity identity;

    public LocalUpRuntime(Identity identity) {
        this.identity = identity;
    }

    @Override
    public Set<UpEngine> getEngines() {
        return Collections.singleton(LocalUpInstance.get().getEngine());
    }

    public void ensureExistsEnvironment(String environmentName) {
        environments.putIfAbsent(environmentName, new LocalEnvironment(LocalEnvironmentManager.get(environmentName)));
    }

    @Override
    public Set<Environment> getEnvironments() {
        return Collections.unmodifiableSet(new HashSet<>(environments.values()));
    }

    @Override
    public Environment getEnvironment(String environmentName) {
        return environments.get(environmentName);
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

}
