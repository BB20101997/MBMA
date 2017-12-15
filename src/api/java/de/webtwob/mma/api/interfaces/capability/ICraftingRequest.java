package de.webtwob.mma.api.interfaces.capability;

import de.webtwob.mma.api.capability.APICapabilities;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public interface ICraftingRequest {

    /**
     * @param stack the ItemStack this will perform on
     *
     * @return the items ICraftingRequest Instance if it has that Capability else null
     */
    static ICraftingRequest getCraftingRequest(ItemStack stack) {
        return !(stack == null || stack.isEmpty()) ? stack.getCapability(
                APICapabilities.CAPABILITY_CRAFTING_REQUEST, null) : null;
    }

    @Nonnull
    ItemStack getRequest();

    void setRequest(@Nonnull ItemStack stack);

    default boolean isCompleted() {
        return getRequest().isEmpty() || getQuantity() <= 0;
    }

    int getQuantity();

    void setQuantity(int amount);

    /**
     * @param i by how much this requests quantity is reduced
     */
    default void reduceQuantity(int i) {
        setQuantity(getQuantity() - i);
    }

}
