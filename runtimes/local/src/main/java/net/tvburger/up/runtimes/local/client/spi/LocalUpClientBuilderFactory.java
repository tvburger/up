package net.tvburger.up.runtimes.local.client.spi;

import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.client.spi.UpClientBuilderFactory;
import net.tvburger.up.runtimes.local.client.LocalUpClientBuilder;
import net.tvburger.up.runtimes.local.client.LocalUpClientTarget;

public final class LocalUpClientBuilderFactory implements UpClientBuilderFactory {

    @Override
    public boolean supportsTarget(UpClientTarget target) {
        return target instanceof LocalUpClientTarget;
    }

    @Override
    public UpClientBuilder createClientBuilder(UpClientTarget target) {
        return new LocalUpClientBuilder((LocalUpClientTarget) target);
    }

}
