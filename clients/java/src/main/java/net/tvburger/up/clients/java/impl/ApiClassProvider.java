package net.tvburger.up.clients.java.impl;

import net.tvburger.up.clients.java.ApiException;

import java.net.MalformedURLException;

public final class ApiClassProvider {

    public static final class Factory {

        public static ApiClassProvider create(String target) throws MalformedURLException {
            return new ApiClassProvider();
        }

        private Factory() {
        }

    }

    private ApiClassProvider() {
    }

    public Class<?> getClass(String className) throws ApiException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException cause) {
            throw new ApiException("Failed to load class: " + className, cause);
        }
    }

}
