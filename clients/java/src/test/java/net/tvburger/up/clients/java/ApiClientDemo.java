package net.tvburger.up.clients.java;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.util.Identities;

public final class ApiClientDemo {

    public static void main(String[] args) throws UpException, Exception {
        UpClient client = UpClient.newBuilder(new ApiClientTarget("http://Thomass-MacBook-Air.local:59210/api"))
                .withEnvironment("dev")
                .withIdentity(Identities.ANONYMOUS)
                .build();
        UpEnvironment environment = client.getEnvironment();
        UpEnvironment.Manager manager = environment.getManager();
//        manager.deploy();
        manager.start();
    }

}
