package net.tvburger.up.applications.api.deploy.impl;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ZipResourceLoader {

    public static final String TOPOLOGY_PATH = "topology.json";

    private final ZipFile zipFile;

    public ZipResourceLoader(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    public File getSourceFile() {
        return new File(zipFile.getName());
    }

    public InputStream openResource(String path) throws IOException {
        try {
            ZipEntry entry = zipFile.getEntry(path);
            return entry == null ? null : zipFile.getInputStream(entry);
        } catch (IOException | IllegalStateException cause) {
            throw new IOException("Failed to open resource: " + path, cause);
        }
    }

    public Reader openTopology() throws IOException {
        InputStream in = openResource(TOPOLOGY_PATH);
        if (in == null) {
            throw new IOException("No topology found!");
        }
        return new InputStreamReader(in);
    }

}
