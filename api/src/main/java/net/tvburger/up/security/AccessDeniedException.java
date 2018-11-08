package net.tvburger.up.security;

import net.tvburger.up.UpException;

public class AccessDeniedException extends UpException {

    public AccessDeniedException() {
        super();
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}
