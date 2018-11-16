package net.tvburger.up.runtime.util;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.topology.TopologyException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class UpServices {

    public static <T> T instantiateService(UpEnvironment environment, Class<T> serviceClass, Object... arguments) throws TopologyException {
        try {
            Constructor<T> constructor = getConstructor(serviceClass, arguments);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0; i < arguments.length; i++) {
                if (parameterTypes[i].equals(arguments[i]) && !parameterTypes[i].equals(Class.class)) {
                    Object serviceInterface = environment.lookupService(parameterTypes[i]);
                    if (serviceInterface == null) {
                        throw new TopologyException("No dependent service found: " + parameterTypes[i]);
                    }
                    arguments[i] = serviceInterface;
                }
            }
            return constructor.newInstance(arguments);
        } catch (ClassCastException | InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException cause) {
            throw new TopologyException(cause);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> getConstructor(Class<T> serviceClass, Object[] arguments) throws TopologyException {
        int argumentLength = arguments == null ? 0 : arguments.length;
        Constructor<?>[] constructors = serviceClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == argumentLength) {
                return (Constructor<T>) constructor;
            }
        }
        throw new TopologyException(String.format("No constructor of %s found with %s arguments!", serviceClass, argumentLength));
    }

    private UpServices() {
    }

}
