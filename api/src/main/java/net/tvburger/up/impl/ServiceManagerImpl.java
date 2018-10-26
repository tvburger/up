package net.tvburger.up.impl;

import net.tvburger.up.ServiceInfo;
import net.tvburger.up.admin.ServiceManager;

public class ServiceManagerImpl<T> implements ServiceManager<T> {

    private final ServiceInfo<T> serviceInfo;
    private boolean logged;

    public ServiceManagerImpl(ServiceInfo<T> serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    @Override
    public ServiceInfo<T> getServiceInfo() {
        return serviceInfo;
    }

    @Override
    public boolean isLogged() {
        return logged;
    }

    @Override
    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
