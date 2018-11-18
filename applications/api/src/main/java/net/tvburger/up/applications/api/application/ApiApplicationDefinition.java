package net.tvburger.up.applications.api.application;

import net.tvburger.up.applications.api.application.impl.DirResourceLoaderUp;
import net.tvburger.up.applications.api.application.impl.JsonTopologyReader;
import net.tvburger.up.topology.TopologyException;
import net.tvburger.up.topology.UpApplicationDefinition;
import net.tvburger.up.topology.UpApplicationTopology;

import java.io.IOException;
import java.io.InputStream;

public final class ApiApplicationDefinition implements UpApplicationDefinition {

    public static final class Factory {

        public static ApiApplicationDefinition create(UpApplicationTopology topology, UpApplicationResourceLoader loader) throws TopologyException {
            return new ApiApplicationDefinition(topology, new UpApplicationClassLoader(loader), loader);
        }

        public static ApiApplicationDefinition create(String basePath) throws TopologyException {
            UpApplicationResourceLoader loader = new DirResourceLoaderUp(basePath);
            try (JsonTopologyReader reader = new JsonTopologyReader(loader.openTopology())) {
                return create(reader.readTopology(), loader);
            } catch (IOException cause) {
                throw new TopologyException("Failed read application definition: " + cause.getMessage(), cause);
            }
        }

        private Factory() {
        }

    }

    private final UpApplicationTopology topology;
    private final UpApplicationClassLoader classLoader;
    private final UpApplicationResourceLoader resourceLoader;

    public ApiApplicationDefinition(UpApplicationTopology topology, UpApplicationClassLoader classLoader, UpApplicationResourceLoader resourceLoader) {
        this.topology = topology;
        this.classLoader = classLoader;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public UpApplicationTopology getTopology() {
        return topology;
    }

    @Override
    public Class<?> getClass(String path) throws TopologyException {
        try {
            return classLoader.loadClass(path);
        } catch (ClassNotFoundException cause) {
            throw new TopologyException(cause);
        }
    }

    @Override
    public InputStream getResource(String path) throws TopologyException {
        try {
            return resourceLoader.openResource(path);
        } catch (IOException cause) {
            throw new TopologyException(cause);
        }
    }

}
