package net.tvburger.up.admin;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface EnvironmentManager {

    void dump(OutputStream out) throws IOException;

    void restore(InputStream in) throws IOException;

    void clear();

    default void pause() {
        throw new NotImplementedException();
    }

    default void resume() {
        throw new NotImplementedException();
    }

}
