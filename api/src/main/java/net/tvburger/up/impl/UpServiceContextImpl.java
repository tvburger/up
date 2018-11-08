package net.tvburger.up.impl;

import net.tvburger.up.Environment;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.behaviors.MutableComposition;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.UpServiceContext;
import net.tvburger.up.deploy.UpEngine;
import net.tvburger.up.security.Identity;

/**
 * This class is a composition that implements the UpServiceContext. The implementation allows to change the fields of this
 * class, but callers that set, must make sure that immutable from perspective of UpServiceContext remains in tact.
 */
public class UpServiceContextImpl implements UpServiceContext, MutableComposition {

    private ServiceInfo<?> serviceInfo;
    private Identity identity;
    private Environment environment;
    private Locality locality;
    private UpEngine engine;

    @Override
    public ServiceInfo<?> getInfo() {
        return serviceInfo;
    }

    public void setInfo(ServiceInfo<?> serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    @Override
    public UpEngine getEngine() {
        return engine;
    }

    public void setEngine(UpEngine engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return String.format("UpServiceContext{%s, %s, %s, %s, %s}", serviceInfo, identity, environment, locality, engine);
    }

}
