package net.tvburger.up.deploy;

import net.tvburger.up.UpException;

public class DeployException extends UpException {

    public DeployException() {
        super();
    }

    public DeployException(Throwable cause) {
        super(cause);
    }

    public DeployException(String message) {
        super(message);
    }

    public DeployException(String message, Throwable cause) {
        super(message, cause);
    }

}
