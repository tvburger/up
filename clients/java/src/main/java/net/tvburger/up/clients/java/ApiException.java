package net.tvburger.up.clients.java;

public class ApiException extends RuntimeException {

    public ApiException() {
        super();
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
