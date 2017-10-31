package de.webtwob.mbma.api.util;

import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

import static de.webtwob.mbma.api.capability.APICapabilities.*;

/**
 * Created by BB20101997 on 29. Mär. 2017.
 */
@SuppressWarnings("WeakerAccess")
public class MBMAFilter {

    public static final Predicate<ICraftingRequest> REQUEST_COMPLETE = ICraftingRequest::isCompleted;
    public static final Predicate<ICraftingRequest> REQUEST_NOT_DONE = REQUEST_COMPLETE.negate();
    public static final Predicate<ItemStack> RECIPE_FILTER = (e) -> e != null && e.hasCapability(
            CAPABILITY_CRAFTING_RECIPE, null);
    public static final Predicate<ItemStack> LINK_FILTER = (e) -> e != null && e.hasCapability(
            CAPABILITY_BLOCK_POS, null);
    public static final Predicate<ItemStack> MUSTER_FILTER = (e) -> e != null && checkIfNotNull(
            e.getCapability(CAPABILITY_CRAFTING_REQUEST, null), REQUEST_NOT_DONE);
    public static final Predicate<ItemStack> INPUT_FILTER = (e) -> e != null && checkIfNotNull(
            e.getCapability(CAPABILITY_CRAFTING_REQUEST, null), REQUEST_COMPLETE);

    private MBMAFilter() {
    }

    private static <T> boolean checkIfNotNull(T o, Predicate<T> cond) {
        return o != null && cond.test(o);
    }

}
