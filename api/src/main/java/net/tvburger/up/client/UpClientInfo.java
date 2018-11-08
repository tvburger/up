package net.tvburger.up.client;

import net.tvburger.up.EnvironmentInfo;
import net.tvburger.up.behaviors.ManagedEntity;

public interface UpClientInfo extends ManagedEntity.Info {

    EnvironmentInfo getEnvironmentInfo();

}
