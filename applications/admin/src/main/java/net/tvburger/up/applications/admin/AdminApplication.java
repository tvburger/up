package net.tvburger.up.applications.admin;

import net.tvburger.up.Up;
import net.tvburger.up.UpException;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

@ApplicationPath("/")
public final class AdminApplication extends Application {

    @Override
    public Set<Object> getSingletons() {
        try {
            return Collections.singleton(new RuntimeResource(Up.getContext().getEnvironment().getRuntime()));
        } catch (UpException cause) {
            throw new ExceptionInInitializerError(cause);
        }
    }

}
