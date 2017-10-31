package de.webtwob.mbma.core.common.crafting;

import de.webtwob.mbma.api.exceptions.CraftingException;
import de.webtwob.mbma.api.exceptions.RecoverableCraftingException;
import de.webtwob.mbma.api.interfaces.ICraftingAccessor;
import de.webtwob.mbma.api.interfaces.ICraftingNode;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.core.common.MBMALog;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by BB20101997 on 22. Apr. 2017.
 */
public class CraftingTree {

    private ICraftingRequest request;
    @Nullable
    private ICraftingNode currentNode;

    public CraftingTree(ICraftingRequest request, ICraftingAccessor icnip) {
        this.request = request;
        currentNode = new RecipeSelectCraftingNode(null, request, icnip);

    }

    /**
     * Runs the next cycle on the Tree
     *
     * @throws CraftingException if something caused the crafting operation to fail
     */
    public void nextCycle() throws CraftingException {
        if (currentNode == null) {
            if (!request.isCompleted()) {
                throw new CraftingException("Crafting request not completed and next node was null!");
            }
        } else {
            try {
                Optional<ICraftingNode> nextNode = currentNode.cycle();
                currentNode = nextNode.orElse(null);
            } catch (RecoverableCraftingException e) {
                currentNode = e.recoverNode().orElseThrow(() -> new CraftingException("RecoverableCraftingException without recover Node specified!"));
            } catch (NullPointerException | ArithmeticException e) {
                MBMALog.error("An exception of class {} occurred in a CraftingNodes's cycle Method this should not happen!", e.getClass());
                throw new CraftingException("A NullPointer or Arithmetic Exception occurred in a CraftingNode's cycle method.", e);
            }
        }
    }
}
