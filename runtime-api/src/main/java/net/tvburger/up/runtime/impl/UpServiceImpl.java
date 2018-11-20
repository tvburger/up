package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpService;
import net.tvburger.up.security.Identification;

public class UpServiceImpl<T> implements UpService<T> {

    private final UpApplication application;
    private final UpService.Manager<T> manager;
    private final T service;

    public UpServiceImpl(UpApplication application, Manager<T> manager, T service) {
        this.application = application;
        this.manager = manager;
        this.service = service;
    }

    @Override
    public UpService.Manager<T> getManager() {
        return manager;
    }

    @Override
    public UpService.Info<T> getInfo() {
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
    public UpApplication getApplication() {
        return application;
    }

    @Override
    public String toString() {
        return String.format("UpService{%s}", getInfo());
    }
}
