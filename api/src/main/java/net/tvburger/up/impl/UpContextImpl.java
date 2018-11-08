package net.tvburger.up.impl;

import net.tvburger.up.Environment;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.behaviors.MutableComposition;
import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.UpContext;
import net.tvburger.up.security.Identity;

public class UpContextImpl implements UpContext, MutableComposition {

    private CallerInfo callerInfo;
    private ServiceInfo<?> serviceInfo;
    private Identity identity;
    private Environment environment;
    private Locality locality;

    @Override
    public CallerInfo getCallerInfo() {
        return callerInfo;
    }

    public void setCallerInfo(CallerInfo callerInfo) {
        this.callerInfo = callerInfo;
    }

    @Override
    public ServiceInfo<?> getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(ServiceInfo<?> serviceInfo) {
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
    public String toString() {
        return String.format("UpContext{%s, %s, %s, %s, %s}", callerInfo, serviceInfo, identity, environment, locality);
    }

}
