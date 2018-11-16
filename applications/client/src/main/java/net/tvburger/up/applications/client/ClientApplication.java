package net.tvburger.up.applications.client;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

@ApplicationPath("/javascript")
public final class ClientApplication extends Application {

    public Set<Class<?>> getClasses() {
        return Collections.singleton(ClientResource.class);
    }

}
