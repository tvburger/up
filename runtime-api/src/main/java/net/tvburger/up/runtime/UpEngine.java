package net.tvburger.up.runtime;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.behaviors.Specification;

import java.net.InetAddress;
import java.util.Set;
import java.util.UUID;

public interface UpEngine extends ManagedEntity<UpEngine.Manager, UpEngine.Info> {

    interface Info extends Specification, ManagedEntity.Info {

        UUID getUuid();

        InetAddress getHost();

        int getPort();

    }

    interface Manager extends Implementation, LifecycleManager, ManagedEntity.Manager<Info> {
    }

    Set<Class<?>> listEndpointTypes();

    Set<UpEndpointTechnologyInfo> listEndpointTechnologies();

    <T, I extends UpEndpoint.Info> UpEndpointTechnology<T, I> getEndpointTechnology(Class<T> endpointType);

    <T, I extends UpEndpoint.Info> UpEndpointTechnology<T, I> getEndpointTechnology(UpEndpointTechnologyInfo technologyInfo);

    UpRuntime getRuntime();

}
