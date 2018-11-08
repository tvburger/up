package net.tvburger.up.behaviors;

/**
 * Used to mark a Type that it can be configured for logging.
 */
public interface LogManager {

    boolean isLogged();

    void setLogged(boolean logged);

}
