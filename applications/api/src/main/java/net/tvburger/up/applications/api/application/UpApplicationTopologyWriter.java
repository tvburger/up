package net.tvburger.up.applications.api.application;

import net.tvburger.up.topology.UpApplicationTopology;

import java.io.IOException;
import java.io.Writer;

public abstract class UpApplicationTopologyWriter extends Writer {

    private final Writer writer;

    public UpApplicationTopologyWriter(Writer writer) {
        this.writer = writer;
    }

    public abstract void writeTopology(UpApplicationTopology topology) throws IOException;

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

}
