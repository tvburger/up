package net.tvburger.up.impl;

import net.tvburger.up.UpClient;
import net.tvburger.up.UpContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ServiceUtil {

    public static <T> T instantiateService(Class<T> serviceClass, Object... arguments) {
        return instantiateService(UpContext.getEnvironment().getClient(UpContext.getIdentity()), serviceClass, arguments);
    }

    public static <T> T instantiateService(UpClient upClient, Class<T> serviceClass, Object... arguments) {
        try {
            Constructor<T> constructor = getConstructor(serviceClass, arguments);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0; i < arguments.length; i++) {
                if (parameterTypes[i].equals(arguments[i]) && !parameterTypes[i].equals(Class.class)) {
                    arguments[i] = upClient.getService(parameterTypes[i]).getService();
                }
            }
            return constructor.newInstance(arguments);
        } catch (ClassCastException | InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException cause) {
            throw new IllegalArgumentException(cause);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> getConstructor(Class<T> serviceClass, Object[] arguments) {
        int argumentLength = arguments == null ? 0 : arguments.length;
        Constructor<?>[] constructors = serviceClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == argumentLength) {
                return (Constructor<T>) constructor;
            }
        }
        throw new IllegalArgumentException();
    }

    private ServiceUtil() {
    }

}
