package net.tvburger.up.admin;

import net.tvburger.up.EnvironmentInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface EnvironmentManager extends Lifecycle {

    EnvironmentInfo getEnvironmentInfo();

    void dump(OutputStream out) throws IOException;

    void restore(InputStream in) throws IOException;

    void clear();

}
