package my.company.example.logic;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

@ApplicationPath("/app")
public class HelloApplication extends Application {

    public Set<Class<?>> getClasses() {
        return Collections.singleton(HelloResource.class);
    }

}
