package net.tvburger.up;

public interface ServiceManager<T> {

    ServiceInfo<T> getServiceInfo();

    boolean isLogged();

    void setLogged(boolean logged);

}
