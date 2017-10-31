package de.webtwob.mbma.core.common.crafting;

import de.webtwob.mbma.api.exceptions.CraftingException;
import de.webtwob.mbma.api.interfaces.ICraftingAccessor;
import de.webtwob.mbma.api.interfaces.ICraftingNode;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRecipe;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by BB20101997 on 23. Apr. 2017.
 */
public class VanillaCraftingNode extends ExternalCraftingNode {
    /**
     * All requests past to this constructor will be assumed to be modifiable.
     * If you do NOT want your request to be changed copy it first!
     *
     * @param parent
     * @param request
     * @param icnip
     */
    public VanillaCraftingNode(@Nullable CraftingNode parent, @Nonnull ICraftingRequest request, @Nonnull ICraftingAccessor icnip, ICraftingRecipe recipe, RecipePage page) {
        super(parent, request, icnip, recipe, page);
    }

    @Override
    public void pushItemStackDown(int stackID) {

    }

    @Override
    public Optional<ICraftingNode> cycle() throws CraftingException {
        return Optional.empty();
    }
}
