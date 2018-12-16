package net.tvburger.up.applications.api.jaxrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tvburger.up.applications.api.impl.ApiRoot;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public final class ApiApplicationApplication extends Application {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<>();
        singletons.add(new JsonBodyReader(mapper));
        singletons.add(new JsonBodyWriter(mapper));
        singletons.add(new ApiExceptionMapper());
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return Collections.singleton(ApiRoot.class);
    }

}
