package net.tvburger.up;

import net.tvburger.up.local.LocalUpClientBuilder;

public final class Up {

    public static UpClient createClient() {
        return createClientBuilder().build();
    }

    public static UpClientBuilder createClientBuilder() {
        return new LocalUpClientBuilder();
    }

}
