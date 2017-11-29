package de.webtwob.mma.api.exceptions;

/**
 * Created by BB20101997 on 23. Apr. 2017.
 */
public class NoRecipeException extends RecoverableCraftingException {

    public NoRecipeException(String message) {
        super(message);
    }

    public NoRecipeException(Throwable cause) {
        super(cause);
    }

    public NoRecipeException(String message, Throwable cause) {
        super(message, cause);
    }
}
