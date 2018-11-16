package net.tvburger.up.client.spi;

import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientTarget;

public interface UpClientBuilderFactory {

    boolean supportsTarget(UpClientTarget target);

    UpClientBuilder createClientBuilder(UpClientTarget target);

}
