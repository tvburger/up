package net.tvburger.up;

public class UpException extends Exception {

    public UpException() {
        super();
    }

    public UpException(Throwable cause) {
        super(cause);
    }

    public UpException(String message) {
        super(message);
    }

    public UpException(String message, Throwable cause) {
        super(message, cause);
    }

}
