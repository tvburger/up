package net.tvburger.up;

import net.tvburger.up.admin.ServiceManager;
import net.tvburger.up.identity.Entity;

public interface Service<T> extends Entity {

    ServiceManager<T> getManager();

    ServiceInfo<T> getInfo();

    T getService();

}
