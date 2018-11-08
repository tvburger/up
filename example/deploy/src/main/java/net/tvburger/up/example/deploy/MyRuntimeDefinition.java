package net.tvburger.up.example.deploy;

import net.tvburger.up.definitions.UpEngineDefinition;
import net.tvburger.up.definitions.UpRuntimeDefinition;

public class MyRuntimeDefinition extends UpRuntimeDefinition {

    public MyRuntimeDefinition() {
        super(new UpRuntimeDefinition.Builder()
                .withEngineDefinition(new UpEngineDefinition.Builder()
                        .withImplementation(
                                "java",
                                "8",
                                "Oracle Java SE Runtime Environment",
                                "1.8.0_131")
                        .withEndpointImplementation(
                                "servlet",
                                "3.1",
                                "jetty",
                                "9"
                        )
                        .build())
                .build());
    }

}
