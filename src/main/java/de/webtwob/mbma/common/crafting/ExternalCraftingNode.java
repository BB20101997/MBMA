package de.webtwob.mbma.common.crafting;

import de.webtwob.mbma.api.RecipePage;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRecipe;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.exceptions.CraftingException;
import de.webtwob.mbma.api.interfaces.ICraftingAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by BB20101997 on 23. Apr. 2017.
 */
public class ExternalCraftingNode extends CraftingNode {
    
    /**
     * All requests past to this constructor will be assumed to be modifiable.
     * If you do NOT want your request to be changed copy it first!
     *
     * @param parent
     * @param tree
     * @param request
     * @param icnip
     */
    public ExternalCraftingNode(@Nullable CraftingNode parent, @Nonnull CraftingTree tree, @Nonnull ICraftingRequest request, @Nonnull ICraftingAccessor icnip, @Nonnull ICraftingRecipe recipe, @Nonnull RecipePage page) {
        super(parent, tree, request, icnip);
    }
    
    @Override
    protected void pushItemStackDown(int stackID) {
    
    }
    
    @Override
    protected Optional<CraftingNode> cycle() throws CraftingException {
        return null;
    }
}
