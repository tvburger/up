package net.tvburger.up.impl;

import net.tvburger.up.Service;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.admin.ServiceManager;
import net.tvburger.up.identity.Identity;

public class ServiceImpl<T> implements Service<T> {

    private final ServiceManager<T> manager;
    private final T service;

    public ServiceImpl(ServiceManager<T> manager, T service) {
        this.manager = manager;
        this.service = service;
    }

    @Override
    public ServiceManager<T> getManager() {
        return manager;
    }

    @Override
    public ServiceInfo<T> getInfo() {
        return manager.getServiceInfo();
    }

    @Override
    public T getService() {
        return service;
    }

    @Override
    public Identity getIdentity() {
        return manager.getServiceInfo().getServiceIdentity();
    }

}
