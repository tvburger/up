package net.tvburger.up.client;

import net.tvburger.up.UpException;

public class UpClientException extends UpException {

    public UpClientException() {
        super();
    }

    public UpClientException(Throwable cause) {
        super(cause);
    }

    public UpClientException(String message) {
        super(message);
    }

    public UpClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
