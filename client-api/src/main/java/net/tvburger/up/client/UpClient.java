package net.tvburger.up.client;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.client.util.UpClientBuilderFactories;

import java.io.Closeable;

public interface UpClient extends ManagedEntity<UpClient.Manager, UpClient.Info> {

    interface Info extends ManagedEntity.Info {

        UpEnvironment.Info getEnvironmentInfo();

    }

    interface Manager extends Closeable, ManagedEntity.Manager<Info> {
    }

    static UpClientBuilder newBuilder(UpClientTarget target) throws UpClientException {
        return UpClientBuilderFactories.create(target);
    }

    UpEnvironment getEnvironment();

}
