package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpService;
import net.tvburger.up.security.Identification;

import java.util.Objects;

public class UpServiceImpl<T> implements UpService<T> {

    private final UpService.Manager<T> manager;
    private final T service;

    public UpServiceImpl(Manager<T> manager, T service) {
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
    public String toString() {
        return String.format("UpService{%s}", getInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInfo()) * 3 + 7;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof UpService)) {
            return false;
        }
        return Objects.equals(getInfo(), ((UpService<?>) object).getInfo());
    }

}
