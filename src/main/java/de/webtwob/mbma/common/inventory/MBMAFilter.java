package de.webtwob.mbma.common.inventory;

import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.interfaces.ICondition;
import de.webtwob.mbma.api.interfaces.IObjectCondition;
import net.minecraft.item.ItemStack;

import static de.webtwob.mbma.api.capability.APICapabilities.*;

/**
 * Created by BB20101997 on 29. Mär. 2017.
 */
@SuppressWarnings("WeakerAccess")
public class MBMAFilter {
    
    private MBMAFilter() {}
    
    public static final ICondition FALSE = ()->false;
    
    public static final IObjectCondition<ICraftingRequest> REQUEST_COMPlET  = ICraftingRequest::isCompleted;
    public static final IObjectCondition<ICraftingRequest> REQUEST_NOT_DONE = request -> !request.isCompleted();
    
    public static final IObjectCondition<ItemStack> RECIPE_FILTER = (e) -> e != null && e.hasCapability(
            CAPABILITY_CRAFTING_RECIPE, null);
    
    public static final IObjectCondition<ItemStack> LINK_FILTER = (e) -> e != null && e.hasCapability(
            CAPABILITY_BLOCK_POS, null);
    
    public static final IObjectCondition<ItemStack> MUSTER_FILTER = (e) -> e != null && checkIfNotNull(
            e.getCapability(CAPABILITY_CRAFTING_REQUEST, null), REQUEST_NOT_DONE);
    
    public static final IObjectCondition<ItemStack> INPUT_FILTER = (e) -> e != null && checkIfNotNull(
            e.getCapability(CAPABILITY_CRAFTING_REQUEST, null), REQUEST_COMPlET);
    
    private static <T> boolean checkIfNotNull(T o, IObjectCondition<T> cond) {
        return o != null && cond.checkCondition(o);
    }
    
}
