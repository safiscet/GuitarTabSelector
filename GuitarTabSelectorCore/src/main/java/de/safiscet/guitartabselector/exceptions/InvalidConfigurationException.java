package de.safiscet.guitartabselector.exceptions;

/**
 * Created by Stefan Fritsch on 05.06.2017.
 */
public class InvalidConfigurationException extends Exception {

    public InvalidConfigurationException() {
        super();
    }

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }
}
