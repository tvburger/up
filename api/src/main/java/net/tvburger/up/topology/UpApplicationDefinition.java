package net.tvburger.up.topology;

import java.io.InputStream;

public interface UpApplicationDefinition {

    UpApplicationTopology getTopology();

    Class<?> getClass(String path) throws TopologyException;

    InputStream getResource(String path) throws TopologyException;

}
