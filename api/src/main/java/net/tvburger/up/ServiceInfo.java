package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.behaviors.Specification;

import java.util.UUID;

public interface ServiceInfo<T> extends Specification, ManagedEntity.Info {

    Class<T> getServiceType();

    UUID getServiceInstanceId();

    EnvironmentInfo getEnvironmentInfo();

}
