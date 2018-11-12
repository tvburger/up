package my.company.example.runtime;

import net.tvburger.up.technology.jersey2.Jersey2Implementation;
import net.tvburger.up.technology.jetty9.Jetty9Implementation;
import net.tvburger.up.topology.UpEngineDefinition;
import net.tvburger.up.topology.UpRuntimeTopology;
import net.tvburger.up.util.LocalJavaImplementation;

public final class MyDevRuntimeTopology extends UpRuntimeTopology {

    public MyDevRuntimeTopology() {
        super(new UpRuntimeTopology.Builder()
                .withEngineDefinition(new UpEngineDefinition.Builder()
                        .withImplementation(LocalJavaImplementation.get())
                        .withEndpointImplementation(Jetty9Implementation.get())
                        .withEndpointImplementation(Jersey2Implementation.get())
                        .build())
                .build());
    }

}
