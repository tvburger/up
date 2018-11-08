package net.tvburger.up.deploy;

import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleManager;
import net.tvburger.up.behaviors.ManagedEntity;

public interface UpEngineManager extends Implementation, LifecycleManager, ManagedEntity.Manager<UpEngineInfo> {
}
