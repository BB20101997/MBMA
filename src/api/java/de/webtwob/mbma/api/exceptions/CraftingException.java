package de.webtwob.mbma.api.exceptions;

/**
 * Created by BB20101997 on 23. Apr. 2017.
 */
public class CraftingException extends Throwable {

    public CraftingException(String message) {
        super(message);
    }

    public CraftingException(Throwable cause) {
        super(cause);
    }

    public CraftingException(String message, Throwable cause) {
        super(message, cause);
    }

}
