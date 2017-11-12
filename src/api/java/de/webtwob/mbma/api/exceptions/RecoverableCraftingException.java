package de.webtwob.mbma.api.exceptions;

import de.webtwob.mbma.api.interfaces.ICraftingNode;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by BB20101997 on 23. Apr. 2017.
 */
public abstract class RecoverableCraftingException extends CraftingException {
    
    @Nullable
    private transient ICraftingNode recoverNode = null;
    
    public RecoverableCraftingException(String message) {
        super(message);
    }
    
    public RecoverableCraftingException(Throwable cause) {
        super(cause);
    }
    
    public RecoverableCraftingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public void setRecoverNode(@Nullable ICraftingNode recoverNode) {
        this.recoverNode = recoverNode;
    }
    
    /**
     * @return returns the node that should be called next for recovery
     */
    public Optional<ICraftingNode> recoverNode() {
        return Optional.ofNullable(recoverNode);
    }
}
