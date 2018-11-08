package net.tvburger.up.example.deploy;

import net.tvburger.up.definitions.UpEngineDefinition;
import net.tvburger.up.definitions.UpRuntimeDefinition;
import net.tvburger.up.local.LocalJavaImplementation;
import net.tvburger.up.technology.jetty9.Jetty9Implementation;

public final class MyRuntimeDefinition extends UpRuntimeDefinition {

    public MyRuntimeDefinition() {
        super(new UpRuntimeDefinition.Builder()
                .withEngineDefinition(new UpEngineDefinition.Builder()
                        .withImplementation(LocalJavaImplementation.get())
                        .withEndpointImplementation(Jetty9Implementation.get())
                        .build())
                .build());
    }

}
