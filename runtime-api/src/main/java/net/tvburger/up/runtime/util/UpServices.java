package net.tvburger.up.runtime.util;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.deploy.DeployException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class UpServices {

    public static <T> T instantiateService(UpEnvironment environment, Class<T> serviceClass, Object... arguments) throws DeployException {
        try {
            Objects.requireNonNull(environment);
            Objects.requireNonNull(serviceClass);
            Constructor<T> constructor = getConstructor(serviceClass, arguments);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0; i < arguments.length; i++) {
                if (parameterTypes[i].equals(arguments[i]) && !parameterTypes[i].equals(Class.class)) {
                    Object serviceInterface = environment.lookupService(parameterTypes[i]);
                    if (serviceInterface == null) {
                        throw new DeployException("No dependent service found: " + parameterTypes[i]);
                    }
                    arguments[i] = serviceInterface;
                }
            }
            return constructor.newInstance(arguments);
        } catch (ClassCastException | InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException cause) {
            throw new DeployException(cause);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> getConstructor(Class<T> serviceClass, Object[] arguments) throws DeployException {
        int argumentLength = arguments == null ? 0 : arguments.length;
        Constructor<?>[] constructors = serviceClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == argumentLength) {
                return (Constructor<T>) constructor;
            }
        }
        throw new DeployException(String.format("No constructor of %s found with %s arguments!", serviceClass, argumentLength));
    }

    private UpServices() {
    }

}
