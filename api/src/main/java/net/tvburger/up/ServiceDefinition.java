package net.tvburger.up;

public interface ServiceDefinition {

    interface Builder {

        Builder withServiceClass(Class<?> serviceClass);

        Builder withConstructorArguments(Object[]... argument);

        Builder withServiceProperties(ServiceProperties serviceProperties);

        Builder withInterface(Class<?>... iface);

        void isValid();

        ServiceDefinition build();

    }


    ServiceProperties getServiceProperties();

    Class<?> getInterfaces();

    DeploymentStrategy getDeploymentStrategy();

    LookupStrategy getLookupStrategy();

    Class<?> getServiceClass();

    Object[] getConstructorArguments();

}
