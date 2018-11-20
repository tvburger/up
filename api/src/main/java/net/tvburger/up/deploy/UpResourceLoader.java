package net.tvburger.up.deploy;

import java.io.IOException;
import java.io.InputStream;

public interface UpResourceLoader {

    boolean hasResource(String resourceName);

    InputStream loadResource(String resourceName) throws ResourceNotFoundException, IOException;

}
