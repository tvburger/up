package net.tvburger.up.impl;

import net.tvburger.up.Service;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.ServiceManager;
import net.tvburger.up.security.Identification;

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
        return manager.getInfo();
    }

    @Override
    public T getInterface() {
        return service;
    }

    @Override
    public Identification getIdentification() {
        return manager.getInfo().getIdentification();
    }

    @Override
    public String toString() {
        return String.format("Service{%s}", getInfo());
    }

}
