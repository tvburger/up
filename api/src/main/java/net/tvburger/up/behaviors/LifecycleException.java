package net.tvburger.up.behaviors;

import net.tvburger.up.UpException;

public class LifecycleException extends UpException {

    public LifecycleException() {
        super();
    }

    public LifecycleException(Throwable cause) {
        super(cause);
    }

    public LifecycleException(String message) {
        super(message);
    }

    public LifecycleException(String message, Throwable cause) {
        super(message, cause);
    }

}
