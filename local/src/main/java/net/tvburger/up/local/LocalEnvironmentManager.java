package net.tvburger.up.local;

import net.tvburger.up.Environment;
import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.admin.EnvironmentManager;
import net.tvburger.up.identity.Identity;
import net.tvburger.up.impl.EnvironmentInfoImpl;
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
        return environments.computeIfAbsent(environment, (key) -> {
            EnvironmentInfo info = new EnvironmentInfoImpl(key, Identity.ANONYMOUS);
            return new LocalEnvironmentManager(info, new LocalServicesManager(info, logger));
        });
    }

    private final EnvironmentInfo environmentInfo;
    private final Environment environment;
    private final LocalServicesManager localServicesManager;

    public LocalEnvironmentManager(EnvironmentInfo environmentInfo, LocalServicesManager localServicesManager) {
        this.environmentInfo = environmentInfo;
        this.environment = new LocalEnvironment(this);
        this.localServicesManager = localServicesManager;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public LocalServicesManager getLocalServicesManager() {
        return localServicesManager;
    }

    @Override
    public EnvironmentInfo getEnvironmentInfo() {
        return environmentInfo;
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

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }
}
