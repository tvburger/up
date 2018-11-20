package net.tvburger.up.applications.api.deploy;

import net.tvburger.up.deploy.UpApplicationDefinition;

import java.io.IOException;
import java.io.Reader;

public abstract class UpApplicationDefinitionReader extends Reader {

    private final Reader reader;

    public UpApplicationDefinitionReader(Reader reader) {
        this.reader = reader;
    }

    public abstract UpApplicationDefinition readDefinition() throws IOException;

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}
