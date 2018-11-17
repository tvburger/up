package net.tvburger.up.clients.java;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.util.Identities;

import java.util.Map;
import java.util.Set;

public final class ApiClientDemo {

    public static void main(String[] args) throws UpException {
        UpClient client = UpClient.newBuilder(new ApiClientTarget("http://Thomass-MacBook-Air.local:64725/api"))
                .withEnvironment("dev")
                .withIdentity(Identities.ANONYMOUS)
                .build();
        System.out.println(client.getEnvironment().listServices());
        Map<Specification, Set<? extends UpEndpoint.Info>> map = client.getEnvironment().listEndpoints();
        System.out.println(map.entrySet().iterator().next().getKey().getClass());
        System.out.println(map.entrySet().iterator().next().getKey().getSpecificationName());
    }

}
