package net.tvburger.up.admin;

import net.tvburger.up.ServiceInfo;

public interface ServiceManager<T> {

    ServiceInfo<T> getServiceInfo();

    boolean isLogged();

    void setLogged(boolean logged);

}
