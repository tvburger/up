package net.tvburger.up.util;

import net.tvburger.up.Environment;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.security.AccessDeniedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class Services {

    public static <T> T instantiateService(Environment environment, Class<T> serviceClass, Object... arguments) throws AccessDeniedException, DeployException {
        try {
            Constructor<T> constructor = getConstructor(serviceClass, arguments);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0; i < arguments.length; i++) {
                if (parameterTypes[i].equals(arguments[i]) && !parameterTypes[i].equals(Class.class)) {
                    arguments[i] = environment.getService(parameterTypes[i]).getInterface();
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

    private Services() {
    }

}
