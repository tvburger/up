package net.tvburger.up.runtimes.local.spi;

import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientTarget;
import net.tvburger.up.runtimes.local.impl.LocalUpClientTarget;
import net.tvburger.up.spi.UpClientBuilderFactory;

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
