package my.company.example.runtime;

import net.tvburger.up.definition.UpEngineDefinition;
import net.tvburger.up.definition.UpRuntimeDefinition;
import net.tvburger.up.technology.jetty9.Jetty9Implementation;
import net.tvburger.up.util.LocalJavaImplementation;

public final class MyApplicationDevRuntime extends UpRuntimeDefinition {

    public MyApplicationDevRuntime() {
        super(new UpRuntimeDefinition.Builder()
                .withEngineDefinition(new UpEngineDefinition.Builder()
                        .withImplementation(LocalJavaImplementation.get())
                        .withEndpointImplementation(Jetty9Implementation.get())
                        .build())
                .build());
    }

}
