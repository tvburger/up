package net.tvburger.up.runtimes.local.impl.client;

import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.client.spi.UpClientBuilderFactory;
import net.tvburger.up.runtimes.local.client.LocalClientTarget;

public final class LocalClientBuilderFactory implements UpClientBuilderFactory {

    @Override
    public boolean supportsTarget(UpClientTarget target) {
        return target instanceof LocalClientTarget;
    }

    @Override
    public UpClientBuilder createClientBuilder(UpClientTarget target) {
        return new LocalClientBuilder((LocalClientTarget) target);
    }

}
