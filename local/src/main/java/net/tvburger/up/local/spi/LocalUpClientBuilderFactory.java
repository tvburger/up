package net.tvburger.up.local.spi;

import net.tvburger.up.UpClientBuilder;
import net.tvburger.up.spi.UpClientBuilderFactory;

public class LocalUpClientBuilderFactory implements UpClientBuilderFactory {

    @Override
    public UpClientBuilder createClientBuilder() {
        return new LocalUpClientBuilder();
    }

}
