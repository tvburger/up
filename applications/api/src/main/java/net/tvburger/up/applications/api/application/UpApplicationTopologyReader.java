package net.tvburger.up.applications.api.application;

import net.tvburger.up.topology.UpApplicationTopology;

import java.io.IOException;
import java.io.Reader;

public abstract class UpApplicationTopologyReader extends Reader {

    private final Reader reader;

    public UpApplicationTopologyReader(Reader reader) {
        this.reader = reader;
    }

    public abstract UpApplicationTopology readTopology() throws IOException;

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}
