package net.tvburger.up;

public interface UpClientBuilder {

    String getEnvironment();

    UpClientBuilder withEnvironment(String environment);

    boolean isValid();

    UpClient build();

}
