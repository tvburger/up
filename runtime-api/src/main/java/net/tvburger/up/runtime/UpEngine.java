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

    <T, I extends UpEndpoint.Info> UpEndpointTechnology<I> getEndpointTechnology(Class<T> endpointType);

    default <I extends UpEndpoint.Info> UpEndpointTechnology<I> getEndpointTechnology(Specification specification) {
        for (Class<?> endpointType : listEndpointTypes()) {
            UpEndpointTechnology<?> endpointTechnology = getEndpointTechnology(endpointType);
            UpEndpointTechnologyInfo info = endpointTechnology.getInfo();
            if (info.getSpecificationName().equals(specification.getSpecificationName())
                    && info.getSpecificationVersion().equals(specification.getSpecificationVersion())) {
                return getEndpointTechnology(info);
            }
        }
        return null;
    }

    <I extends UpEndpoint.Info> UpEndpointTechnology<I> getEndpointTechnology(UpEndpointTechnologyInfo technologyInfo);

    UpRuntime getRuntime();

}
