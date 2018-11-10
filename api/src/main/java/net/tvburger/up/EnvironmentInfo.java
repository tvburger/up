package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.runtime.UpRuntimeInfo;

public interface EnvironmentInfo extends ManagedEntity.Info {

    String getName();

    UpRuntimeInfo getRuntimeInfo();

}
