package net.tvburger.up.local;

import net.tvburger.up.admin.EnvironmentManager;
import net.tvburger.up.logger.Logger;
import net.tvburger.up.logger.impl.ConsoleLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LocalEnvironmentManager implements EnvironmentManager {

    private static final Map<String, LocalEnvironmentManager> environments = new HashMap<>();
    private static final Logger logger = new ConsoleLogger();

    public static LocalEnvironmentManager get(String environment) {
        return environments.computeIfAbsent(environment, (key) -> new LocalEnvironmentManager(key, new LocalServicesManager(key, logger)));
    }

    private final String environment;
    private final LocalServicesManager localServicesManager;

    public LocalEnvironmentManager(String environment, LocalServicesManager localServicesManager) {
        this.environment = environment;
        this.localServicesManager = localServicesManager;
    }

    public LocalServicesManager getLocalServicesManager() {
        return localServicesManager;
    }

    public String getEnvironment() {
        return environment;
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
