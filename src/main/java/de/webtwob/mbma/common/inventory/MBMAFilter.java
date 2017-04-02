package de.webtwob.mbma.common.inventory;

import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.interfaces.IObjectCondition;
import net.minecraft.item.ItemStack;

import java.util.Objects;

import static de.webtwob.mbma.api.capability.APICapabilities.CAPABILITY_BLOCK_POS;
import static de.webtwob.mbma.api.capability.APICapabilities.CAPABILITY_CRAFTING_REQUEST;

/**
 * Created by BB20101997 on 29. Mär. 2017.
 */
@SuppressWarnings("WeakerAccess")
public class MBMAFilter {
    
    private MBMAFilter() {}
    
    public static final IObjectCondition<Object> FALSE = (o) -> false;
    
    public static final IObjectCondition<ICraftingRequest> REQUEST_EMPTY    = request -> request.getRequest().isEmpty() || request.isCompleted();
    public static final IObjectCondition<ICraftingRequest> REQUEST_NOT_DONE = request -> !request.getRequest().isEmpty() && !request.isCompleted();
    
    public static final IObjectCondition<ItemStack> RECIPE_FILTER = Objects::nonNull;//TODO expand to check if valid recipe
    
    public static final IObjectCondition<ItemStack> LINK_FILTER   = (e) -> e != null && e.hasCapability(
            CAPABILITY_BLOCK_POS, null);
    
    public static final IObjectCondition<ItemStack> MUSTER_FILTER = (e) -> e != null && checkIfNotNull(
            e.getCapability(CAPABILITY_CRAFTING_REQUEST, null), REQUEST_NOT_DONE);
    
    public static final IObjectCondition<ItemStack> INPUT_FILTER  = (e) -> e != null && checkIfNotNull(
            e.getCapability(CAPABILITY_CRAFTING_REQUEST, null), REQUEST_EMPTY);
    
    private static <T> boolean checkIfNotNull(T o, IObjectCondition<T> cond) {
        return o != null && cond.checkCondition(o);
    }
    
}
