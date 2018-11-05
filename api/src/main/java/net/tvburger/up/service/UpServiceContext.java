package net.tvburger.up.service;

import net.tvburger.up.ServiceInfo;
import net.tvburger.up.deploy.UpLanguageInterpreter;

public interface UpServiceContext {

    UpLanguageInterpreter getLanguageInterpreter();

    InfraTraceElement getLocation();

    ServiceInfo<?> getServiceInfo();

}
