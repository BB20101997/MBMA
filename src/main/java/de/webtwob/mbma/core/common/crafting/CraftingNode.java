package de.webtwob.mbma.core.common.crafting;

import de.webtwob.mbma.api.exceptions.CraftingException;
import de.webtwob.mbma.api.interfaces.ICraftingAccessor;
import de.webtwob.mbma.api.interfaces.ICraftingNode;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by BB20101997 on 22. Apr. 2017.
 */
public abstract class CraftingNode implements ICraftingNode{

    @Nullable
    protected CraftingNode parent;
    protected ICraftingRequest request;
    protected boolean doneStatus = true;
    protected ICraftingAccessor iCraftingAccessor;

    /**
     * All requests past to this constructor will be assumed to be modifiable.
     * If you do NOT want your request to be changed copy it first!
     */
    public CraftingNode(@Nullable CraftingNode parent, @Nonnull ICraftingRequest request, @Nonnull ICraftingAccessor icnip) {
        this.parent = parent;
        this.request = request;
        iCraftingAccessor = icnip;
    }

    /**
     * used by childes to pass the items to the parent node
     * since items should be stored in the ICraftingAccessor
     * only the id needs to be given to the parent
     */
    public abstract void pushItemStackDown(int stackID);

    /**
     * Does one cycle of the crafting operation
     * this is supposed to be called every tick and it
     * should not do all at once.
     *
     * @return the next node to be called cycle up on
     * null might be returned by the RootNode
     * @throws CraftingException in case of error
     */
    public abstract Optional<ICraftingNode> cycle() throws CraftingException;
}
