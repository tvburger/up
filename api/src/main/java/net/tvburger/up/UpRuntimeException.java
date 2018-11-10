package net.tvburger.up;

public class UpRuntimeException extends RuntimeException {

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
