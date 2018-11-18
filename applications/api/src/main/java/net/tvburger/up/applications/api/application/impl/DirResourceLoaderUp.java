package net.tvburger.up.applications.api.application.impl;

import net.tvburger.up.applications.api.application.UpApplicationResourceLoader;

import java.io.*;

public final class DirResourceLoaderUp implements UpApplicationResourceLoader {

    public static final String TOPOLOGY_PATH = "topology.json";

    private final String basePath;

    public DirResourceLoaderUp(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public InputStream openResource(String path) throws IOException {
        File file = new File(basePath + "/" + path);
        if (file.exists() && !file.canRead()) {
            throw new IOException("Can't open resource: " + path);
        }
        return file.exists() ? new FileInputStream(basePath + "/" + path) : null;
    }

    @Override
    public Reader openTopology() throws IOException {
        InputStream in = openResource(TOPOLOGY_PATH);
        if (in == null) {
            throw new IOException("No topology found!");
        }
        return new InputStreamReader(in);
    }

}
