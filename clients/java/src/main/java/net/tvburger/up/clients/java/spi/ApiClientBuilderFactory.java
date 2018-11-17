package net.tvburger.up.clients.java.spi;

import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.client.spi.UpClientBuilderFactory;
import net.tvburger.up.clients.java.ApiClientTarget;
import net.tvburger.up.clients.java.impl.ApiClientBuilder;

public final class ApiClientBuilderFactory implements UpClientBuilderFactory {

    @Override
    public boolean supportsTarget(UpClientTarget target) {
        return target instanceof ApiClientTarget;
    }

    @Override
    public UpClientBuilder createClientBuilder(UpClientTarget target) {
        return new ApiClientBuilder((ApiClientTarget) target);
    }

}
