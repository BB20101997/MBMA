package de.webtwob.mbma.common.crafting;

import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.exceptions.CraftingException;
import de.webtwob.mbma.api.interfaces.ICraftingAccessor;
import net.minecraft.item.ItemStack;

import java.util.Optional;

/**
 * Created by BB20101997 on 23. Apr. 2017.
 *
 */
public class RootNode extends RecipeSelectCraftingNode {
    public RootNode(CraftingTree craftingTree, ICraftingRequest request, ICraftingAccessor icnip) {
        super(null, craftingTree, request, icnip);
        gather = false;
    }
    
    @Override
    protected void pushItemStackDown(int stackID) {
        ItemStack pushed = iCraftingAccessor.peekAtItemInStorage(stackID);
        if(pushed.isItemEqual(request.getRequest())){
            iCraftingAccessor.pushResult(stackID);
        }else{
            iCraftingAccessor.dropExcess(stackID);
        }
    }
    
    @Override
    protected Optional<CraftingNode> cycle() throws CraftingException {
       return Optional.ofNullable(tryToCraft());
    }
    
    /**
     * the root node cannot gather
     */
    @Override
    protected CraftingNode tryToGather() {
        return this;
    }
}
