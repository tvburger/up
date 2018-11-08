package net.tvburger.up.client;

import net.tvburger.up.Environment;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.security.AccessDeniedException;

public interface UpClient extends ManagedEntity<UpClientManager, UpClientInfo> {

    Environment getEnvironment() throws AccessDeniedException;

}
