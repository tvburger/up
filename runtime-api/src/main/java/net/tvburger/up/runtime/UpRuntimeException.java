package net.tvburger.up.runtime;

import net.tvburger.up.UpException;

public class UpRuntimeException extends UpException {

    public UpRuntimeException() {
        super();
    }

    public UpRuntimeException(Throwable cause) {
        super(cause);
    }

    public UpRuntimeException(String message) {
        super(message);
    }

    public UpRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
