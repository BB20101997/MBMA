package de.webtwob.mbma.api.interfaces.capability;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

/**
 * Created by BB20101997 on 08. Nov. 2017.
 */
@FunctionalInterface
public interface ICraftingRequestProvider {
    
    /**
     * @param require the condition a request must satisfy
     * @return the first element of the queue satisfying the requirement or else null
     */
    ItemStack getRequestIfRequirementHolds(Predicate<ItemStack> require);
    
}
