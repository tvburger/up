package net.tvburger.up.applications.api.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public interface UpApplicationResourceLoader {

    InputStream openResource(String path) throws IOException;

    Reader openTopology() throws IOException;

}
