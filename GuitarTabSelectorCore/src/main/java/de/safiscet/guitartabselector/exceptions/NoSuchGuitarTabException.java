package de.safiscet.guitartabselector.exceptions;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
public class NoSuchGuitarTabException extends Exception {

    public NoSuchGuitarTabException() {
        super();
    }

    public NoSuchGuitarTabException(String message) {
        super(message);
    }

    public NoSuchGuitarTabException(Throwable cause) {
        super(cause);
    }

    public NoSuchGuitarTabException(String message, Throwable cause) {
        super(message, cause);
    }
}
