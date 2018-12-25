package net.tvburger.up.applications.admin;

import net.tvburger.up.runtime.context.UpContext;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

@ApplicationPath("/admin")
public final class AdminApplication extends Application {

    @Override
    public Set<Object> getSingletons() {
        return Collections.singleton(new EnvironmentResource(UpContext.getContext().getEnvironment()));
    }

}
