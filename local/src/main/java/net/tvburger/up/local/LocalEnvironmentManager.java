package net.tvburger.up.local;

import net.tvburger.up.EnvironmentManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LocalEnvironmentManager implements EnvironmentManager {

    private static final Map<String, LocalEnvironmentManager> environments = new HashMap<>();

    public static LocalEnvironmentManager get(String environment) {
        return environments.computeIfAbsent(environment, (key) -> new LocalEnvironmentManager(new LocalServicesManager()));
    }

    private final LocalServicesManager localServicesManager;

    public LocalEnvironmentManager(LocalServicesManager localServicesManager) {
        this.localServicesManager = localServicesManager;
    }

    public LocalServicesManager getLocalServicesManager() {
        return localServicesManager;
    }

    @Override
    public void dump(OutputStream out) throws IOException {

    }

    @Override
    public void restore(InputStream in) throws IOException {

    }

    @Override
    public void clear() {
    }

}
