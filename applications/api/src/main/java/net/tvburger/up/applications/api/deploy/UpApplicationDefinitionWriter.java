package net.tvburger.up.applications.api.deploy;

import net.tvburger.up.deploy.UpApplicationDefinition;

import java.io.IOException;
import java.io.Writer;

public abstract class UpApplicationDefinitionWriter extends Writer {

    private final Writer writer;

    public UpApplicationDefinitionWriter(Writer writer) {
        this.writer = writer;
    }

    public abstract void writeDefinition(UpApplicationDefinition definition) throws IOException;

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
