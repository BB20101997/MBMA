package de.webtwob.mbma.common.crafting;

import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.exceptions.CraftingException;
import de.webtwob.mbma.api.exceptions.RecoverableCraftingException;
import de.webtwob.mbma.api.interfaces.ICraftingAccessor;
import de.webtwob.mbma.common.MBMALog;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by BB20101997 on 22. Apr. 2017.
 */
public class CraftingTree {
    
    private ICraftingRequest request;
    @Nullable
    private CraftingNode currentNode;
    private RootNode rootNode;
    
    public CraftingTree(ICraftingRequest request, ICraftingAccessor icnip) {
        this.request = request;
        currentNode = rootNode = new RootNode(this, request, icnip);
        
    }
    
    /**
     * Runs the next cycle on the Tree
     *
     * @throws CraftingException if something caused the crafting operation to fail
     */
    public void nextCycle() throws CraftingException {
        if (currentNode == null) {
            if (request.isCompleted()) {
                return;
            } else {
                throw new CraftingException("Crafting request not completed and next node was null!");
            }
        }
        try {
            Optional<CraftingNode> nextNode = currentNode.cycle();
            currentNode = nextNode.orElse(null);
        } catch (RecoverableCraftingException e) {
            currentNode = e.recoverNode().orElseThrow(() -> new CraftingException("RecoverableCraftingException with no recover Node specified!"));
        } catch (CraftingException e) {
            //recoverable only if currentNode is not null and the cause is recoverable
            throw new CraftingException("The cycle failed because the current node threw a CraftingException not of type RecoverableCraftingException.", e);
        } catch (Exception e) {
            //an unchecked error must have occurred those should be converted to a CraftingFailed Exception
            MBMALog.error("An exception of class {} occurred in a CraftingNodes's cycle Method this should not happen!", e.getClass());
            throw new CraftingException("An unknown error occurred in a CraftingNode's cycle method.", e);
        }
    }
    
}
